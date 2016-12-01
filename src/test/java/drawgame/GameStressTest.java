package drawgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.drawgame.Game;
import com.drawgame.RegistrableToGame;
import com.drawgame.client.drawcomponent.Drawing;
import com.drawgame.client.drawcomponent.Stroke;

public class GameStressTest {
	
	private static final int NUMBER_OF_CLIENTS = 100;
	private static final int NUMBER_OF_STROKES_PER_CLIENT = 200;
	
	private Game game;
	
	private ArrayList<TestClient> testClients;
	
	@Before
	public void setUp() {
		game = new Game();
		testClients = new ArrayList<TestClient>(NUMBER_OF_CLIENTS);
		
		for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
			testClients.add(new TestClient(i, game));
		}
	}
	
	@Test
	public void testAddStroke() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		
		for (TestClient client : testClients) {
			client.start();
		}
		
		for (TestClient client : testClients) {
			client.join();
		}
		
		long duration = System.currentTimeMillis() - startTime;
		
		ArrayList<Stroke> serverStrokes = game.getDrawing().getStrokesAsArrayList();
		assertEquals(
				"server misses strokes", 
				NUMBER_OF_CLIENTS * NUMBER_OF_STROKES_PER_CLIENT, serverStrokes.size());
		
		for (TestClient client : testClients) {
			System.out.println("needed " + duration + "ms " 
					+ "to add " + NUMBER_OF_CLIENTS * NUMBER_OF_STROKES_PER_CLIENT + " strokes, " 
					+ "validate outcome: " + client.id + "/" + (testClients.size()-1) + " " 
					+ "(List.contains(..) consumes time)");
			
			ArrayList<Stroke> clientStrokes = client.gameComponent.getDrawing().getStrokesAsArrayList();
			assertEquals("client " + client.id + " misses strokes", serverStrokes.size(), clientStrokes.size());
			
			for (Stroke serverStroke : serverStrokes) {
				assertTrue("client " + client.id + " misses stroke", clientStrokes.contains(serverStroke));
			}
		}
	}
	
	private class StressTestGameComponent implements RegistrableToGame {

		private final Game game;
		
		private Drawing drawing = new Drawing();
		
		public StressTestGameComponent(Game game) {
			this.game = game;
		}
		
		public void addStrokeToServer(Stroke stroke) {
//			System.out.println("sending stroke to server");
			drawing.addStroke(stroke);
			game.addStroke(stroke, this);
		}
		
		public Drawing getDrawing() {
			return drawing;
		}
		
		@Override
		public void addStrokeToClient(Stroke stroke) {
//			System.out.println("received stroke from server");
			drawing.addStroke(stroke);
		}

		@Override
		public void loadComponent() {
			drawing = game.getDrawing();
		}
		
	}
	
	private class TestClient extends Thread {
		
		public final int id;
		
		public final StressTestGameComponent gameComponent;
		
		public TestClient(int id, Game game) {
			this.id = id;
			this.gameComponent = new StressTestGameComponent(game);
			game.registerComponent(gameComponent);
		}
		
		@Override
		public void run() {
			for (int i = 0; i < NUMBER_OF_STROKES_PER_CLIENT; i++) {
				gameComponent.addStrokeToServer(new Stroke());
			}
		}
	}
	
}
