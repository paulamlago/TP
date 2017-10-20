package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.tp.Junio2016_1.PeonesAction;
import es.ucm.fdi.tp.Junio2016_1.PeonesState;
import es.ucm.fdi.tp.Junio2016_1.PeonesView;
import es.ucm.fdi.tp.Junio2016_2.FyHaction;
import es.ucm.fdi.tp.Junio2016_2.FyHstate;
import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.*;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessState;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.view.ConsoleController;
import es.ucm.fdi.tp.view.ConsoleView;
import es.ucm.fdi.tp.view.GUIController;
import es.ucm.fdi.tp.view.GUIView;
import es.ucm.fdi.tp.view.GameContainer;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.panelComponents.MessageViewer;
import es.ucm.fdi.tp.view.panelComponents.PlayersInfoViewer;
import es.ucm.fdi.tp.view.panelComponents.Board.ChessView;
import es.ucm.fdi.tp.view.panelComponents.Board.TttView;
import es.ucm.fdi.tp.view.panelComponents.Board.WasView;
import es.ucm.fdi.tp.view.panelComponents.Board.fhView;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.MessageViewerComp;
import es.ucm.fdi.tp.view.panelComponents.concreteClasses.PlayersInfoComp;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class Main {

	private static GameTable<?, ?> createGame(String gType) {	// create a game with a GameState depending on the value of gType

		switch(gType.toUpperCase()){
			case "TTT":  return new GameTable<TttState, TttAction>(new TttState(3));
			case "WAS": return new GameTable<WolfAndSheepState, WolfAndSheepAction> (new WolfAndSheepState(8));
			case "CHESS": return new GameTable<ChessState, ChessAction> (new ChessState());
			case "PEONES": return new GameTable<PeonesState, PeonesAction> (new PeonesState());
			case "FH": return new GameTable<FyHstate, FyHaction>(new FyHstate());
			
			default: return null;
		}

	}
	
	private static <S extends GameState<S, A>, A extends GameAction<S,A>> void startConsoleMode(String gType, GameTable<S, A> game,String playerModes[]) {
		
		List<GamePlayer> players = new ArrayList<GamePlayer>(); // create the list of players as in assignment 4
		int i = 0;
		while (i < playerModes.length){
			players.add(createPlayer(gType, playerModes[i], "Player " + i));
			i++;
		}			
			new ConsoleView<S,A>(game);
			new ConsoleController<S,A>(players,game).run();
		
	}
	
	private static <S extends GameState<S, A>, A extends GameAction<S,A>> void startGUIMode(final String gType, final GameTable<S, A> game, String playerModes[]) {
		ExecutorService executor = null;
		
		for (int i = 0; i < game.getState().getPlayerCount(); i++) { //I need to create 2 views for 2 players
			final GamePlayer p1 = new RandomPlayer("Player " + i);
			final ConcurrentAiPlayer p2 = new ConcurrentAiPlayer("Player " + i);
			
			p1.join(i);
			p2.join(i);
			
			final int x = i;
			
			executor = Executors.newSingleThreadExecutor();
			GameController<S, A> gameCtrl = new GUIController<S,A>(x, p1, p2, game, executor);
					
			GUIView<S,A> guiView = (GUIView<S, A>) createGameView(gType, gameCtrl);
					
			GUIView<S,A> container = new GameContainer<S,A>(guiView, gameCtrl, game);
			container.enableWindowMode(x);
		}
		
		executor.submit(new Runnable() {
			 @Override
			 public void run() {
				 game.start();
			 }
		});
	}
	
	@SuppressWarnings("unchecked")
	public static <S extends GameState<S, A>, A extends GameAction<S,A>> GUIView<S, A> createGameView(String gType, GameController<S, A> gameCtrl){
		 GUIView<S, A> guiView = null;
		
		if (gType.toUpperCase().equalsIgnoreCase("TTT")){
			PlayersInfoViewer<TttState, TttAction> piv = new PlayersInfoComp<TttState, TttAction>(2);
			MessageViewer<TttState, TttAction> mv = new MessageViewerComp<TttState, TttAction>();
						
			guiView = (GUIView<S, A>) new TttView((GameController<TttState, TttAction>)gameCtrl, mv, piv, 3, 3);
		}
		else if (gType.toUpperCase().equalsIgnoreCase("WAS")){
			PlayersInfoViewer<WolfAndSheepState, WolfAndSheepAction> piv = new PlayersInfoComp<WolfAndSheepState, WolfAndSheepAction>(2);
			MessageViewer<WolfAndSheepState, WolfAndSheepAction> mv = new MessageViewerComp<WolfAndSheepState, WolfAndSheepAction>();
			
			guiView = (GUIView<S, A>) new WasView((GameController<WolfAndSheepState, WolfAndSheepAction>)gameCtrl, mv, piv, 8, 8);
		}
		else if (gType.toUpperCase().equalsIgnoreCase("CHESS")){
			PlayersInfoViewer<ChessState, ChessAction> piv = new PlayersInfoComp<ChessState, ChessAction>(2);
			MessageViewer<ChessState, ChessAction> mv = new MessageViewerComp<ChessState, ChessAction>();
			
			guiView = (GUIView<S, A>) new ChessView((GameController<ChessState, ChessAction>)gameCtrl, mv, piv, 8, 8);
		}
		else if(gType.toUpperCase().equalsIgnoreCase("PEONES")){
			PlayersInfoViewer<PeonesState, PeonesAction> piv = new PlayersInfoComp<PeonesState, PeonesAction>(2);
			MessageViewer<PeonesState, PeonesAction> mv = new MessageViewerComp<PeonesState, PeonesAction>();
			
			guiView = (GUIView<S, A>) new PeonesView((GameController<PeonesState, PeonesAction>)gameCtrl, mv, piv, 4, 4);
		}
		else if (gType.toUpperCase().equalsIgnoreCase("FH")){
			PlayersInfoViewer<FyHstate, FyHaction> piv = new PlayersInfoComp<FyHstate, FyHaction>(2);
			MessageViewer<FyHstate, FyHaction> mv = new MessageViewerComp<FyHstate, FyHaction>();
			
			guiView = (GUIView<S, A>) new fhView((GameController<FyHstate, FyHaction>)gameCtrl, mv, piv, 8, 8);			
		}
		return guiView;
	}

	public static GamePlayer createPlayer(String gameName, String playerType, String playerName) throws GameError{
		//1. We've got different types of players: manual, random or Smart
		
		if (playerType.toUpperCase().equalsIgnoreCase("MANUAL")){
			return new ConsolePlayer(playerName, new Scanner(System.in));
		}
		else if (playerType.toUpperCase().equalsIgnoreCase("RAND")){
			return new RandomPlayer(playerName);
		}
		else if (playerType.toUpperCase().equalsIgnoreCase("SMART")){
			return new SmartPlayer(playerName, 3);
		}
		else throw new GameError("Error: player " + playerType + " is not defined.");
	}
	
	private static void usage() {
		System.out.println("MAIN USAGE: " + "\n" + "game (ttt or was) + mode (gui or console) + player (manual, random or smart)." + "\n");
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			usage();
			System.exit(1);
		}
		
		GameTable<?, ?> game = createGame(args[0]);
		
		if (game == null) {
			System.err.println("Invalid game");
			System.exit(1);
		}
		
		String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
		
		switch (args[1]) {
			case "console":
				startConsoleMode(args[0],game,otherArgs);
			break;
			case "gui":
				startGUIMode(args[0],game,otherArgs);
			break;
			default:
				System.err.println("Invalid view mode: "+ args[1]);
				usage();
				System.exit(1);
		}
	}
}


