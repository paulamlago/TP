package es.ucm.fdi.tp.view.panelComponents.concreteClasses;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.GUIView;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;

public class SmartMovesPanel<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S, A>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton stop;
	protected JLabel brain;
	
	private MessageViewer<S, A> infoViewer;
	
	public interface SmartMoveObserver {
		public void onStart();
		public void onEnd(boolean success, long time, int nodes, double value);
	}
	
	private GameController<S, A> gameCtrl;
	
	
	public SmartMovesPanel(GameController<S, A> gameCtrl) {
		this.gameCtrl = gameCtrl;
		initGUI();
		this.setBorder(BorderFactory.createTitledBorder("Smart Moves"));
	}

	private void initGUI() {
		brain = new JLabel(new ImageIcon("src/main/resources/brain.png"));
		brain.setOpaque(true);
		brain.setBackground(getBackground());
		this.add(brain, BorderLayout.EAST);
		
		SpinnerModel sm = new SpinnerNumberModel(3,1 , Runtime.getRuntime().availableProcessors(), 1);
		JSpinner selector = new JSpinner(sm);
		selector.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
				gameCtrl.smartPlayerConcurrencyLevel((int) selector.getValue());
			}
		});
		
		this.add(selector, BorderLayout.CENTER);
		
		JLabel hilos = new JLabel("Threads");
		this.add(hilos);
		
		JLabel reloj = new JLabel(new ImageIcon("src/main/resources/timer.png"));
		this.add(reloj);
		
		SpinnerModel model = new SpinnerNumberModel(1000, 500, 5000, 500);
		JSpinner Timespinner = new JSpinner(model);

		Timespinner.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				gameCtrl.smartPlayerTimeLimit((int) Timespinner.getValue());
	
			}
			
		});
		this.add(Timespinner);
		JLabel sec = new JLabel("ms");
		this.add(sec);
		
		stop = new JButton(new ImageIcon("src/main/resources/stop.png"));
		stop.setEnabled(false);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameCtrl.stopSmartPlayer();
				infoViewer.addContent("You have stopped the smart player!");
				brain.setBackground(getBackground());
				stop.setEnabled(false);			
			}
		});
		this.add(stop, BorderLayout.WEST);
	}

	@Override
	public void enable() {
		stop.setEnabled(true);
		
	}

	@Override
	public void disable() {
		stop.setEnabled(false);
		
	}

	@Override
	public void update(S state) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameController(GameController<S, A> gameCtrl) {
		// TODO Auto-generated method stub
		
	}
	
	public void setBrainBackground(Color c){
		brain.setBackground(c);
	}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		this.infoViewer = infoViewer;
	}

}
