package es.ucm.fdi.tp.view.panelComponents.concreteClasses;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import es.ucm.fdi.tp.base.PlayerMode;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.GUIView;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.SmartMovesPanel.SmartMoveObserver;

public class ControlPanel<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S, A> implements SmartMoveObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GameController<S, A> gameCtrl;
	private JButton random;
	private JButton smart;
	private JButton restart;
	private JButton exit;
	private JButton bomba;
	
	private JComboBox<String> mode;
	private JComboBox<Integer> dBomba;
	
	boolean active = false;
	
	private SmartMovesPanel<S, A> smartMoves;
	private MessageViewer<S, A> messageViewer;
	
	public ControlPanel(GameController<S, A> gameCtrl) {
		this.gameCtrl = gameCtrl;
		
		initGUI();
		this.setBorder(BorderFactory.createTitledBorder("Control Panel"));
		this.gameCtrl.addSmartPlayerObserver(this);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initGUI() { 
		ImageIcon icon;
		
		//create the buttons + combo box + make their action listeners call corresponding methods of the game controller.
		//Better call the controller methods using SwingUtilities.invokeLater
	
		JPanel buttonPanel = new JPanel();

		random = new JButton();
		icon = new ImageIcon("src/main/resources/dice.png");
		random.setIcon(icon);
		random.setEnabled(active);
		buttonPanel.add(random);
		
		random.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameCtrl.makeRandomMove();
				
			}
		});
		
		
		smart = new JButton();
		//smart.setLocation(50, 30);
		icon = new ImageIcon("src/main/resources/nerd.png");
		smart.setIcon(icon);
		buttonPanel.add(smart);
		smart.setEnabled(active);
		smart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameCtrl.makeSmartMove();
				
			}
		});
		
		restart = new JButton();
		//restart.setLocation(70, 30);

		icon = new ImageIcon("src/main/resources/restart.png");
		restart.setIcon(icon);
		buttonPanel.add(restart);
		restart.setEnabled(true);
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameCtrl.restratGame();
				
			}
		});
		
		exit = new JButton();
		//exit.setLocation(90, 30);

		icon = new ImageIcon("src/main/resources/exit.png");
		exit.setIcon(icon);
		buttonPanel.add(exit);
		exit.setEnabled(true);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				confirmationWindow();
				
			}
		});
		
		JLabel label = new JLabel("Player Mode: ");
		buttonPanel.add(label);
		
		mode = new JComboBox(PlayerMode.values());
		if (gameCtrl.getPlayerMode() == PlayerMode.manual) mode.setSelectedIndex(0);
		else if (gameCtrl.getPlayerMode() == PlayerMode.random) mode.setSelectedIndex(1);
		else mode.setSelectedIndex(2);

		buttonPanel.add(mode);

		mode.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				gameCtrl.changePlayerMode((PlayerMode)mode.getSelectedItem());
			}
			
		});

		this.add(buttonPanel, BoxLayout.X_AXIS);
		
		dBomba = new JComboBox();
		dBomba.addItem(1);
		dBomba.addItem(2);
		dBomba.addItem(3);
		dBomba.addItem(4);
		dBomba.addItem(5);
		buttonPanel.add(dBomba);
		
		dBomba.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameCtrl.dBomba((int) dBomba.getSelectedItem());
			}
		});
		
		this.add(dBomba);
		
		bomba = new JButton("bomba");
		
		this.add(bomba);
		bomba.setEnabled(true);
		bomba.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				gameCtrl.bomba();
			}
			
		});
		
		JPanel SmartMoves = new JPanel();
		smartMoves = new SmartMovesPanel<S, A>(gameCtrl);
		SmartMoves.add(this.smartMoves);
		this.add(SmartMoves);
		
		
	}

	@Override
	public void enable() {
		active = true;
		random.setEnabled(active);
		smart.setEnabled(active);
	}

	@Override
	public void disable() {
		active = false;
		random.setEnabled(active);
		smart.setEnabled(active);
	}

	@Override
	public void update(S state) {
		if (gameCtrl.getPlayerId() == state.getTurn()){
			enable();
		}
		else disable();
	}
	
	public void confirmationWindow(){
		
		int n = JOptionPane.showOptionDialog(new JFrame(),
				"Are sure you want to quit?", "Quit",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);

		if (n == 0) {
			gameCtrl.stopGame();
			System.exit(0);
		}
	}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
	this.messageViewer = infoViewer;
	smartMoves.setMessageViewer(infoViewer);
	}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		// TODO Auto-generated method stub
	}

	public void setGameController(GameController<S, A> gameCtrl) {
		this.gameCtrl = gameCtrl;
	}

	@Override
	public void onStart() {
		smartMoves.enable();
		smartMoves.setBrainBackground(Color.YELLOW);
		messageViewer.addContent("The smart player has started to think");
	}

	@Override
	public void onEnd(boolean success, long time, int nodes, double value) {
		smartMoves.disable();
		smartMoves.setBrainBackground(getBackground());
		if (success == true ) messageViewer.addContent("The execution was completed successfully. It took " + time + "ms and used "+ nodes+ " nodes with a value "+ value);
		else messageViewer.addContent("The execution was interrumpted. It took " + time + "ms and used "+ nodes+ " nodes with a value "+ value);
	}
	
}
