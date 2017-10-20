package es.ucm.fdi.tp.view.panelComponents.concreteClasses;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;

public class MessageViewerComp<S extends GameState<S,A>, A extends GameAction<S,A>> extends MessageViewer<S, A> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea msgArea;
	private JScrollPane scroll;
	
	public MessageViewerComp() { //the constructor just calls the initGUI
		initGUI();
	
		this.setBorder(BorderFactory.createTitledBorder("Message Viewer"));
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		msgArea = new JTextArea(15, 20);
		
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		
		scroll = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(300, 200));
		
		mainPanel.add(scroll);
		//JPanel ctrlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		this.add(mainPanel, BorderLayout.PAGE_START);
	}
	
	//In these methods modif msgArea accordingly

	public void addContent(String msg) {
		msgArea.append(msg+ "\n"); 
	}
	
	public void setContent(String msg) {
		//setContetn deletes the current content and adds a new message
		msgArea.setText(msg + "\n");
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
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
}
