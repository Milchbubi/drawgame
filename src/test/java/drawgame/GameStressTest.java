package drawgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.drawgame.Game;
import com.drawgame.RegistrableToGame;
import com.drawgame.client.drawcomponent.Stroke;

public class GameStressTest {
	
	private static final int NUMBER_OF_CLIENTS = 200;
	private static final int NUMBER_OF_STROKES_PER_CLIENT = 500;
	
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
		for (TestClient client : testClients) {
			client.start();
		}
		
		for (TestClient client : testClients) {
			client.join();
		}
		
		ArrayList<Stroke> serverStrokes = game.getDrawing().getStrokesAsArrayList();
		assertEquals(
				"server misses strokes", 
				NUMBER_OF_CLIENTS * NUMBER_OF_STROKES_PER_CLIENT, serverStrokes.size());
		
		for (TestClient client : testClients) {
			
			ArrayList<Stroke> clientStrokes = client.gameComponent.getStrokes();
			assertEquals("client " + client.id + " misses strokes", serverStrokes.size(), clientStrokes.size());
			
			for (Stroke serverStroke : serverStrokes) {
				assertTrue("client " + client.id + " misses stroke", clientStrokes.contains(serverStroke));
			}
		}
	}
	
	private class StressTestGameComponent implements RegistrableToGame {

		private final Game game;
		
		private ArrayList<Stroke> strokes = 
				new ArrayList<Stroke>(NUMBER_OF_CLIENTS * NUMBER_OF_STROKES_PER_CLIENT);
		
		public StressTestGameComponent(Game game) {
			this.game = game;
		}
		
		public void addStroke(Stroke stroke) {
//			System.out.println("sending stroke to server");
			strokes.add(stroke);
			game.addStroke(stroke, this);
		}
		
		public ArrayList<Stroke> getStrokes() {
			return this.strokes;
		}
		
		@Override
		public void addStrokeToClient(Stroke stroke) {
//			System.out.println("received stroke from server");
			strokes.add(stroke);
		}

		@Override
		public void loadComponent() {
			strokes = game.getDrawing().getStrokesAsArrayList();
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
				gameComponent.addStroke(new Stroke());
			}
		}
	}
	
}
