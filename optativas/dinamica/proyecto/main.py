from __future__ import annotations

import pygame

from voley_lib import (
    AirModel,
    BallConfig,
    Camera,
    CourtConfig,
    SERVE_PRESETS,
    ServeResult,
    ServeState,
    TopspinJumpServeController,
    Volleyball,
    VolleyCourt,
    draw_text_block,
    draw_trajectory,
    format_result,
    setup_space,
)

FPS = 60
SUBSTEPS = 8
SCREEN_WIDTH = 1400
SCREEN_HEIGHT = 720


class App:
    def __init__(self):
        pygame.init()
        pygame.display.set_caption("Comparacion de saques de voleibol")
        self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
        self.clock = pygame.time.Clock()
        self.font = pygame.font.SysFont("Arial", 18)
        self.small_font = pygame.font.SysFont("Arial", 15)
        self.title_font = pygame.font.SysFont("Arial", 24, bold=True)

        self.court_config = CourtConfig()
        self.camera = Camera(
            width=SCREEN_WIDTH,
            height=SCREEN_HEIGHT,
            world_left=self.court_config.visual_left,
            world_right=self.court_config.visual_right,
            world_bottom=self.court_config.visual_bottom,
            world_top=self.court_config.visual_top,
        )
        self.space = setup_space()
        self.court = VolleyCourt(self.space, self.court_config)
        self.ball = Volleyball(
            self.space,
            BallConfig(),
            (self.court_config.serve_x, self.court_config.serve_y),
        )
        self.air = AirModel()
        self.state = ServeState()
        self.topspin_controller = TopspinJumpServeController(
            self.court_config, SERVE_PRESETS["topspin"]
        )
        self.history: list[ServeResult] = []
        self.path_history: list[tuple[tuple[int, int, int], list]] = []
        self.last_serve_key = "float"
        self.show_trajectory = True
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
                    self.state.trajectory.clear()

    def launch(self, key: str) -> None:
        config = SERVE_PRESETS[key]
        self.last_serve_key = key
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

    def update(self, dt: float) -> None:
        if self.topspin_controller.active:
            hit_now = self.topspin_controller.update(self.ball, dt)
            if hit_now:
                config = SERVE_PRESETS["topspin"]
                hit_position = self.topspin_controller.hit_position
                self.ball.reset((hit_position.x, hit_position.y))
                self.ball.launch(config)
                self.air.reset()
                self.state.start(config, self.ball.body.position)

        if not self.state.active or self.state.config is None:
            return

        dt_sub = dt / SUBSTEPS
        for _ in range(SUBSTEPS):
            self.air.apply(self.ball, self.state.config, dt_sub)
            self.space.step(dt_sub)
            result = self.state.update(self.ball, self.court_config, dt_sub)
            if result is not None:
                self.ball.freeze()
                self.history.append(result)
                self.path_history.append(
                    (self.state.config.color, list(self.state.trajectory))
                )
                if (
                    self.topspin_controller.active
                    and self.state.config.key == "topspin"
                ):
                    self.topspin_controller.finish()
                break

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
            self.small_font,
            jumping_serve,
            player_pose,
        )
        if self.show_trajectory:
            self.draw_history_trajectories()
            if self.state.config is not None:
                draw_trajectory(
                    self.screen,
                    self.camera,
                    self.state.trajectory,
                    self.state.config.color,
                )
        self.ball.draw(self.screen, self.camera)
        self.draw_side_panel()
        pygame.display.flip()

    def draw_history_trajectories(self) -> None:
        for color, trajectory in self.path_history[-8:]:
            draw_trajectory(self.screen, self.camera, trajectory, color)

    def draw_side_panel(self) -> None:
        panel_x = 1060
        pygame.draw.rect(
            self.screen,
            (244, 246, 248),
            (panel_x, 0, SCREEN_WIDTH - panel_x, SCREEN_HEIGHT),
        )
        pygame.draw.line(
            self.screen, (190, 195, 200), (panel_x, 0), (panel_x, SCREEN_HEIGHT), 2
        )

        title = self.title_font.render("Saques de voleibol", True, (20, 20, 20))
        self.screen.blit(title, (panel_x + 22, 22))

        controls = [
            "1  Saque flotante",
            "2  Saque topspin",
            "3  Saque globo",
            "ESPACIO  repetir saque",
            "R  reset",
            "T  trayectoria on/off",
            "C  limpiar historial",
        ]
        draw_text_block(
            self.screen, self.font, controls, panel_x + 22, 62, line_height=20
        )

        y = 218
        current_config = self.state.config or SERVE_PRESETS[self.last_serve_key]
        topspin_active = self.topspin_controller.active or (
            self.state.active and current_config.key == "topspin"
        )
        pygame.draw.circle(self.screen, current_config.color, (panel_x + 32, y + 10), 7)
        self.screen.blit(
            self.title_font.render(current_config.name, True, (20, 20, 20)),
            (panel_x + 48, y - 4),
        )
        serve_lines = [
            f"v0 = {current_config.speed:.1f} m/s",
            f"angulo = {current_config.angle_deg:.1f} grados",
            f"spin = {current_config.spin:.1f} rad/s",
            f"Cd = {current_config.cd:.2f}",
            f"k Magnus = {current_config.magnus_k:.2f}",
            f"viento = ({self.air.last_wind.x:.2f}, {self.air.last_wind.y:.2f}) m/s",
            current_config.description,
        ]
        if topspin_active:
            flight_time = (
                self.state.elapsed
                if self.state.active and self.state.config is not None
                else 0.0
            )
            serve_lines.extend(
                [
                    f"Fase: {self.topspin_controller.phase_label()}",
                    f"Alt. golpeo objetivo = "
                    f"{self.topspin_controller.hit_position.y:.2f} m",
                    f"Vel. golpeo = {current_config.speed:.1f} m/s",
                    f"Spin aplicado = {current_config.spin:.1f} rad/s",
                    f"Preparacion = {self.topspin_controller.preparation_time:.2f} s",
                    f"Vuelo post-golpeo = {flight_time:.2f} s",
                ]
            )
        draw_text_block(
            self.screen,
            self.small_font,
            serve_lines,
            panel_x + 22,
            y + 36,
            line_height=16 if topspin_active else 21,
        )

        y = 462 if topspin_active else 420
        self.screen.blit(
            self.title_font.render("Ultimo resultado", True, (20, 20, 20)),
            (panel_x + 22, y),
        )
        draw_text_block(
            self.screen,
            self.small_font if topspin_active else self.font,
            format_result(self.state.result),
            panel_x + 22,
            y + 34,
            line_height=16 if topspin_active else 23,
        )

        y = 615 if topspin_active else 610
        self.screen.blit(
            self.title_font.render("Comparativa", True, (20, 20, 20)), (panel_x + 22, y)
        )
        if not self.history:
            draw_text_block(
                self.screen,
                self.small_font,
                ["Todavia no hay saques."],
                panel_x + 22,
                y + 34,
            )
        else:
            lines = []
            for result in self.history[-4:]:
                landing = (
                    "-" if result.landing_x is None else f"{result.landing_x:.1f}m"
                )
                lines.append(
                    f"{result.serve_name}: {result.result}, t={result.flight_time:.2f}s, x={landing}"
                )
            draw_text_block(
                self.screen,
                self.small_font,
                lines,
                panel_x + 22,
                y + 34,
                line_height=19,
            )


if __name__ == "__main__":
    App().run()
