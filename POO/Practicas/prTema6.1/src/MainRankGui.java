import gui.CtrRank;
import gui.PanelRank;

import javax.swing.*;

public class MainRankGui {
	//----------------------------------------------------------------------
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	//----------------------------------------------------------------------
	private static void createGUI() {
        PanelRank pRank = new PanelRank();
        CtrRank ctrPrim = new CtrRank(pRank);
        pRank.controler(ctrPrim);
		//--------------------------
        JFrame app = new JFrame("Web Rank");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setContentPane(pRank);
        app.pack();
        app.setVisible(true);
    }
}

/*
I->C
J->C
A->C
A->D
B->C
B->F
D->F
E->B
E->H
F->G
F->H
G->E
G->H


 */
