package drawgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.drawgame.Game;
import com.drawgame.RegistrableToGame;
import com.drawgame.client.drawcomponent.Drawing;
import com.drawgame.client.drawcomponent.Stroke;

public class GameTest {
	
	private class TestGameComponent implements RegistrableToGame {
		
		public Stroke addStrokeToClientWasCalled = null;
		public boolean loadComponentWasCalled = false;
		
		@Override
		public void addStrokeToClient(Stroke stroke) {
			addStrokeToClientWasCalled = stroke;
		}
		
		@Override
		public void loadComponent() {
			loadComponentWasCalled = true;
		}
	}
	
	private Game game;
	
	private TestGameComponent gameComponent;
	
	@Before
	public void setUp() {
		game = new Game();
		gameComponent = new TestGameComponent();
	}
	
	@Test
	public void testAddStrokeAndGetDrawing() {
		Stroke stroke0 = new Stroke();
		Stroke stroke1 = new Stroke();
		
		game.addStroke(stroke0, gameComponent);
		game.addStroke(stroke1, gameComponent);
		Drawing drawing = game.getDrawing();
		
		assertEquals(2, drawing.getStrokes().size());
		assertEquals(stroke0, drawing.getStrokes().get(0));
		assertEquals(stroke1, drawing.getStrokes().get(1));
	}
	
	@Test
	public void testClearDrawing() {
		Stroke stroke = new Stroke();
		
		game.addStroke(stroke, gameComponent);
		game.clearDrawing();
		Drawing drawing = game.getDrawing();
		
		assertTrue(drawing.getStrokes().isEmpty());
	}
	
	@Test
	public void testRegisterComponentAndAddStroke() {
		Stroke stroke = new Stroke();
		TestGameComponent gameComponent2 = new TestGameComponent();
		
		game.registerComponent(gameComponent);
		game.registerComponent(gameComponent2);
		game.addStroke(stroke, gameComponent);
		
		assertNull("sender gameComponent should not be notified itself", gameComponent.addStrokeToClientWasCalled);
		assertEquals("gameComponent was not notified", stroke, gameComponent2.addStrokeToClientWasCalled);
	}
	
	@Test
	public void testRegisterComponentAndClearComponent() {
		
		game.registerComponent(gameComponent);
		game.clearDrawing();
		
		assertTrue("gameComponent was not notified to reload after clearing", gameComponent.loadComponentWasCalled);
	}
	
	@Test
	public void testDeregisterComponent() {
		game.registerComponent(gameComponent);
		
		game.deregisterComponent(gameComponent);
		game.clearDrawing();
		
		assertFalse("gameComponent was not deregistered", gameComponent.loadComponentWasCalled);
	}
	
}
