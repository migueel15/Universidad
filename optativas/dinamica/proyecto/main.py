from __future__ import annotations

from dataclasses import replace

import pygame

from voley_lib import (
    AirModel,
    BallConfig,
    Camera,
    CourtConfig,
    NoMagnusTrajectory,
    SERVE_PRESETS,
    ServeResult,
    ServeState,
    TopspinJumpServeController,
    Volleyball,
    VolleyCourt,
    draw_text_block,
    draw_trajectory,
    setup_space,
)

FPS = 60
SUBSTEPS = 8
SCREEN_WIDTH = 1400
SCREEN_HEIGHT = 720
ANGLE_STEP_DEG = 1.0
MIN_SERVE_ANGLE_DEG = -5.0
MAX_SERVE_ANGLE_DEG = 75.0
NO_MAGNUS_TRAJECTORY_COLOR = (142, 68, 173)


class App:
    def __init__(self):
        pygame.init()
        pygame.key.set_repeat(180, 45)
        pygame.display.set_caption("Comparacion de saques de voleibol")
        self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
        self.clock = pygame.time.Clock()
        self.small_font = pygame.font.SysFont("Arial", 15)
        self.title_font = pygame.font.SysFont("Arial", 24, bold=True)

        self.court_config = CourtConfig()
        self.camera = Camera(width=SCREEN_WIDTH, height=SCREEN_HEIGHT)
        self.space = setup_space()
        self.court = VolleyCourt(self.space, self.court_config)
        self.ball = Volleyball(
            self.space,
            BallConfig(),
            (self.court_config.serve_x, self.court_config.serve_y),
        )
        self.air = AirModel()
        self.state = ServeState()
        self.no_magnus_trajectory = NoMagnusTrajectory()
        self.topspin_controller = TopspinJumpServeController(
            self.court_config, SERVE_PRESETS["topspin"]
        )
        self.serve_angles = {
            key: config.angle_deg for key, config in SERVE_PRESETS.items()
        }
        self.history: list[ServeResult] = []
        self.path_history: list[tuple[tuple[int, int, int], list]] = []
        self.no_magnus_path_history: list[list] = []
        self.last_serve_key = "float"
        self.show_trajectory = False
        self.ball_follow_through = False
        self.running = True

    def run(self) -> None:
        while self.running:
            dt = min(self.clock.tick(FPS) / 1000.0, 1.0 / 30.0)
            self.handle_events()
            self.update(dt)
            self.draw()
        pygame.quit()

    def handle_events(self) -> None:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self.running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    self.running = False
                elif event.key == pygame.K_1:
                    self.launch("float")
                elif event.key == pygame.K_2:
                    self.launch("topspin")
                elif event.key == pygame.K_3:
                    self.launch("globo")
                elif event.key == pygame.K_SPACE:
                    self.launch(self.last_serve_key)
                elif event.key == pygame.K_r:
                    self.reset_ball()
                elif event.key == pygame.K_t:
                    self.show_trajectory = not self.show_trajectory
                elif event.key == pygame.K_c:
                    self.history.clear()
                    self.path_history.clear()
                    self.no_magnus_path_history.clear()
                    self.state.trajectory.clear()
                    self.no_magnus_trajectory.reset()
                elif event.key in (pygame.K_UP, pygame.K_RIGHT):
                    self.adjust_serve_angle(ANGLE_STEP_DEG)
                elif event.key in (pygame.K_DOWN, pygame.K_LEFT):
                    self.adjust_serve_angle(-ANGLE_STEP_DEG)

    def serve_config(self, key: str):
        return replace(SERVE_PRESETS[key], angle_deg=self.serve_angles[key])

    def adjust_serve_angle(self, delta_deg: float) -> None:
        current = self.serve_angles[self.last_serve_key]
        adjusted = max(
            MIN_SERVE_ANGLE_DEG,
            min(MAX_SERVE_ANGLE_DEG, current + delta_deg),
        )
        self.serve_angles[self.last_serve_key] = adjusted

    def launch(self, key: str) -> None:
        config = self.serve_config(key)
        self.last_serve_key = key
        self.ball_follow_through = False
        self.no_magnus_trajectory.reset()
        if key == "topspin":
            self.state = ServeState()
            self.air.reset()
            self.topspin_controller.start(self.ball)
            return

        self.topspin_controller.reset()
        launch_position = (
            self.court_config.serve_x,
            config.launch_y or self.court_config.serve_y,
        )
        self.ball.reset(launch_position)
        self.ball.launch(config)
        self.air.reset()
        self.state.start(config, self.ball.body.position)

    def reset_ball(self) -> None:
        self.ball.reset((self.court_config.serve_x, self.court_config.serve_y))
        self.air.reset()
        self.state = ServeState()
        self.topspin_controller.reset()
        self.no_magnus_trajectory.reset()
        self.ball_follow_through = False

    def update(self, dt: float) -> None:
        if self.topspin_controller.active:
            hit_now = self.topspin_controller.update(self.ball, dt)
            if hit_now:
                config = self.serve_config("topspin")
                hit_position = self.topspin_controller.hit_position
                hit_angle = self.ball.body.angle
                self.ball.reset((hit_position.x, hit_position.y), hit_angle)
                self.ball.launch(config)
                self.air.reset()
                self.state.start(config, self.ball.body.position)
                self.no_magnus_trajectory.start(config, self.ball)

        if self.state.active and self.state.config is not None:
            self.update_active_serve(dt)
            return

        if self.ball_follow_through and self.state.config is not None:
            self.update_ball_follow_through(dt)
            return

        if self.no_magnus_trajectory.active:
            self.update_no_magnus_only(dt)

    def update_active_serve(self, dt: float) -> None:
        if self.state.config is None:
            return

        dt_sub = dt / SUBSTEPS
        for _ in range(SUBSTEPS):
            self.air.apply(self.ball, self.state.config, dt_sub)
            self.update_no_magnus_trajectory(dt_sub)
            self.space.step(dt_sub)
            result = self.state.update(self.ball, self.court_config, dt_sub)
            if result is not None:
                self.history.append(result)
                self.path_history.append(
                    (self.state.config.color, list(self.state.trajectory))
                )
                self.ball_follow_through = True
                if (
                    self.topspin_controller.active
                    and self.state.config.key == "topspin"
                ):
                    self.topspin_controller.finish()
                break

    def update_ball_follow_through(self, dt: float) -> None:
        if self.state.config is None:
            return

        dt_sub = dt / SUBSTEPS
        for _ in range(SUBSTEPS):
            self.air.apply(self.ball, self.state.config, dt_sub)
            self.update_no_magnus_trajectory(dt_sub)
            self.space.step(dt_sub)
            if self.ball_is_out_of_screen():
                self.return_ball_to_player()
                break

    def update_no_magnus_trajectory(self, dt: float) -> None:
        if not self.no_magnus_trajectory.active:
            return

        finished = self.no_magnus_trajectory.update(
            self.air.last_wind, self.court_config, dt
        )
        if finished:
            self.no_magnus_path_history.append(
                list(self.no_magnus_trajectory.trajectory)
            )

    def update_no_magnus_only(self, dt: float) -> None:
        dt_sub = dt / SUBSTEPS
        for _ in range(SUBSTEPS):
            self.update_no_magnus_trajectory(dt_sub)

    def ball_is_out_of_screen(self) -> bool:
        center = self.camera.to_screen(self.ball.body.position)
        radius = self.camera.length_to_px(self.ball.radius)
        return (
            center[0] < -radius
            or center[0] > SCREEN_WIDTH + radius
            or center[1] < -radius
            or center[1] > SCREEN_HEIGHT + radius
        )

    def return_ball_to_player(self) -> None:
        self.ball_follow_through = False
        self.air.reset()
        self.topspin_controller.reset()
        self.ball.reset((self.court_config.serve_x, self.court_config.serve_y))

    def draw(self) -> None:
        jumping_serve = (
            self.state.active
            and self.state.config is not None
            and self.state.config.jump_serve
        )
        player_pose = (
            self.topspin_controller.pose if self.topspin_controller.active else None
        )
        self.court.draw(
            self.screen,
            self.camera,
            jumping_serve,
            player_pose,
        )
        if self.show_trajectory:
            self.draw_history_trajectories()
            if self.no_magnus_trajectory.active:
                draw_trajectory(
                    self.screen,
                    self.camera,
                    self.no_magnus_trajectory.trajectory,
                    NO_MAGNUS_TRAJECTORY_COLOR,
                )
            if self.state.config is not None:
                draw_trajectory(
                    self.screen,
                    self.camera,
                    self.state.trajectory,
                    self.state.config.color,
                )
        self.ball.draw(self.screen, self.camera)
        self.draw_top_panel()
        pygame.display.flip()

    def draw_history_trajectories(self) -> None:
        for color, trajectory in self.path_history[-8:]:
            draw_trajectory(self.screen, self.camera, trajectory, color)
        for trajectory in self.no_magnus_path_history[-8:]:
            draw_trajectory(
                self.screen, self.camera, trajectory, NO_MAGNUS_TRAJECTORY_COLOR
            )

    def draw_top_panel(self) -> None:
        panel_height = self.camera.margin_top
        pygame.draw.rect(
            self.screen, (244, 246, 248), (0, 0, SCREEN_WIDTH, panel_height)
        )
        pygame.draw.line(
            self.screen,
            (190, 195, 200),
            (0, panel_height),
            (SCREEN_WIDTH, panel_height),
            2,
        )
        for x in (340, 790, 1080):
            pygame.draw.line(
                self.screen, (215, 220, 225), (x, 14), (x, panel_height - 14), 1
            )

        title = self.title_font.render("Saques de voleibol", True, (20, 20, 20))
        self.screen.blit(title, (22, 14))
        controls = [
            "1 Flotante   2 Topspin   3 Globo",
            "ESPACIO Repetir   R Reset",
            "T Trayectoria   C Limpiar   Flechas angulo",
        ]
        draw_text_block(self.screen, self.small_font, controls, 22, 48, line_height=18)
        zoom = self.small_font.render(
            f"Escala vista: {self.camera.scale:.1f} px/m", True, (85, 85, 85)
        )
        self.screen.blit(zoom, (22, 104))

        current_config = (
            self.state.config
            if self.state.active and self.state.config is not None
            else self.serve_config(self.last_serve_key)
        )
        topspin_active = self.topspin_controller.active or (
            self.state.active and current_config.key == "topspin"
        )
        pygame.draw.circle(self.screen, current_config.color, (360, 28), 7)
        self.screen.blit(
            self.title_font.render(current_config.name, True, (20, 20, 20)), (376, 14)
        )
        serve_lines = [
            f"v0 {current_config.speed:.1f} m/s   "
            f"ang {current_config.angle_deg:.1f} deg   "
            f"spin {current_config.spin:.1f} rad/s",
            f"Cd {current_config.cd:.2f}   kM {current_config.magnus_k:.2f}   "
            f"viento ({self.air.last_wind.x:.2f}, {self.air.last_wind.y:.2f}) m/s",
        ]
        if topspin_active:
            flight_time = (
                self.state.elapsed
                if self.state.active and self.state.config is not None
                else 0.0
            )
            serve_lines.append(
                f"Fase {self.topspin_controller.phase_label()}   "
                f"golpeo {self.topspin_controller.hit_position.y:.2f} m   "
                f"prep {self.topspin_controller.preparation_time:.2f} s   "
                f"vuelo {flight_time:.2f} s"
            )
            serve_lines.append("Tray sin Magnus: morado")
        else:
            serve_lines.append(
                f"Magnus {current_config.magnus_k:.2f}   "
                f"rot.drag {current_config.rotational_drag_cm:.3f}"
            )
        draw_text_block(
            self.screen, self.small_font, serve_lines, 360, 50, line_height=18
        )

        self.screen.blit(
            self.title_font.render("Ultimo resultado", True, (20, 20, 20)), (812, 14)
        )
        result = self.state.result
        if result is None:
            result_lines = ["Sin resultado"]
        else:
            landing = "-" if result.landing_x is None else f"{result.landing_x:.2f} m"
            net = (
                "-"
                if result.net_cross_height is None
                else f"{result.net_cross_height:.2f} m"
            )
            clearance = (
                "-"
                if result.net_clearance is None
                else f"{result.net_clearance:.2f} m"
            )
            result_lines = [
                f"{result.result}   t {result.flight_time:.2f} s   x {landing}",
                f"hmax {result.max_height:.2f} m   red {net}",
                f"margen {clearance}   vfin {result.final_speed:.1f} m/s",
            ]
        draw_text_block(
            self.screen, self.small_font, result_lines, 812, 50, line_height=18
        )

        self.screen.blit(
            self.title_font.render("Comparativa", True, (20, 20, 20)), (1102, 14)
        )
        if not self.history:
            history_lines = ["Todavia no hay saques."]
        else:
            history_lines = []
            for result in self.history[-3:]:
                landing = "-" if result.landing_x is None else f"{result.landing_x:.1f}m"
                serve_name = result.serve_name.replace("Saque ", "").capitalize()
                history_lines.append(
                    f"{serve_name}: {result.result}   "
                    f"t {result.flight_time:.2f}s   x {landing}"
                )
        draw_text_block(
            self.screen, self.small_font, history_lines, 1102, 50, line_height=18
        )


if __name__ == "__main__":
    App().run()
