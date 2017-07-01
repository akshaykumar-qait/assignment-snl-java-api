package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.qainfotech.tap.training.snl.api.Board;
import com.qainfotech.tap.training.snl.api.GameInProgressException;
import com.qainfotech.tap.training.snl.api.MaxPlayersReachedExeption;
import com.qainfotech.tap.training.snl.api.PlayerExistsException;

import static org.assertj.core.api.Assertions.*;

public class BoardTest {

	Board boardreader, boardreader1;

	@BeforeTest
	public void makeobject() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		boardreader = new Board();
	}

	@Test
	public void check_player_register() throws PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException {

		boardreader = new Board();

		assertThat(boardreader.getData()).isNotNull();
		assertThat(boardreader.getUUID()).isNotNull();
		// System.err.println((boardreader.registerPlayer("shadab5")).length());

		assertThat((boardreader.registerPlayer("shadab")).length()).isEqualTo(1);
		assertThat((boardreader.registerPlayer("shadab1")).length()).isEqualTo(2);

		JSONObject hey = (JSONObject) boardreader.data.getJSONArray("players").get(1);

		UUID my = UUID.fromString(hey.get("uuid").toString());
		assertThat(boardreader.deletePlayer(my).length()).isEqualTo(1);

	}

	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
	public void if_playerlist_exceeds_the_number_four() throws UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {

		boardreader1 = new Board();
		boardreader1.registerPlayer("shadab1");
		boardreader1.registerPlayer("shadab2");
		boardreader1.registerPlayer("shadab3");
		boardreader1.registerPlayer("shadab4");
		boardreader1.registerPlayer("shadab5");
	}
	
	@Test(expectedExceptions = GameInProgressException.class)
	public void game_in_progress_exception() throws UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, JSONException, InvalidTurnException {

		boardreader1 = new Board();
		boardreader1.registerPlayer("shadab1");
		boardreader1.registerPlayer("shadab2");
		

		JSONObject jsonobjofplayer_turn = (JSONObject) boardreader1.data.getJSONArray("players")
				.get(Integer.parseInt(boardreader1.data.get("turn").toString()));
		// roll dice
		boardreader1.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));

		boardreader1.registerPlayer("shadab3");
		
		
		
		
	}

	@Test(expectedExceptions = PlayerExistsException.class, expectedExceptionsMessageRegExp = "Player 'shadab' already exists on board")
	public void if_entered_same_player_name_should_return_exception()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {

		boardreader = new Board();
		boardreader.registerPlayer("shadab");
		boardreader.registerPlayer("shadab");

	}

	@Test(expectedExceptions = NoUserWithSuchUUIDException.class, expectedExceptionsMessageRegExp = "No Player with uuid 'fc487af1-e11a-46a2-99de-1c22a69df5da' on board")
	public void check_invalid_uuid_exception()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException {

		boardreader = new Board();
		boardreader.deletePlayer(UUID.fromString("fc487af1-e11a-46a2-99de-1c22a69df5da"));

	}

	@Test(expectedExceptions = InvalidTurnException.class, expectedExceptionsMessageRegExp = "Player 'fc487af1-e11a-46a2-99de-1c22a69df5da' does not have the turn")
	public void check_invalid_turn_exception()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {

		boardreader = new Board();
		boardreader.registerPlayer("akshay1");

		boardreader.registerPlayer("akshay2");

		boardreader.rollDice(UUID.fromString("fc487af1-e11a-46a2-99de-1c22a69df5da"));

	}

	@Test
	public void get_incorrect_roll_of_dice_message()
			throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, JSONException, InvalidTurnException {
		Board board_obj = new Board();
		board_obj.registerPlayer("akshay1");
		board_obj.registerPlayer("akshay2");
		board_obj.registerPlayer("akshay3");
		board_obj.registerPlayer("akshay4");

		for (int playerindex = 0; playerindex <= 3; playerindex++) {
			board_obj.data.getJSONArray("players").getJSONObject(playerindex).put("position", 100);
		}

		JSONObject jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
				.get(Integer.parseInt(board_obj.data.get("turn").toString()));
		// roll dice
		JSONObject output_json = board_obj.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));

		assertThat(output_json.get("message")).isEqualTo("Incorrect roll of dice. Player did not move");

	}
		
	@Test
	public static void check_turn_works_or_not() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, JSONException, InvalidTurnException
	{
		Board board_obj = new Board();
		board_obj.registerPlayer("akshay");
		board_obj.registerPlayer("nishant");	
		board_obj.registerPlayer("shadab");	
		board_obj.registerPlayer("superman");	
		
		
		JSONObject jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
				.get(Integer.parseInt(board_obj.data.get("turn").toString()));
		
		assertThat(jsonobjofplayer_turn.get("name")).isEqualTo("akshay");
		
		// roll dice
		 board_obj.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));
		
		 jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
					.get(Integer.parseInt(board_obj.data.get("turn").toString()));
			
		assertThat(jsonobjofplayer_turn.get("name")).isEqualTo("nishant");

		// roll dice
		 board_obj.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));
		
		 jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
					.get(Integer.parseInt(board_obj.data.get("turn").toString()));
			
		assertThat(jsonobjofplayer_turn.get("name")).isEqualTo("shadab");


		// roll dice
		 board_obj.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));
		
		 jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
					.get(Integer.parseInt(board_obj.data.get("turn").toString()));
			
		assertThat(jsonobjofplayer_turn.get("name")).isEqualTo("superman");


		// roll dice
		 board_obj.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));
		
		 jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
					.get(Integer.parseInt(board_obj.data.get("turn").toString()));
			
		assertThat(jsonobjofplayer_turn.get("name")).isEqualTo("akshay");


		// roll dice
		 board_obj.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));
		
		 jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
					.get(Integer.parseInt(board_obj.data.get("turn").toString()));
			
		assertThat(jsonobjofplayer_turn.get("name")).isEqualTo("nishant");



		
	}
	
	
	@Test
	public void check_ladders()
			throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, JSONException, InvalidTurnException {
		Board board_obj = new Board();
		board_obj.registerPlayer("akshay1");
		board_obj.registerPlayer("akshay2");
		board_obj.registerPlayer("akshay3");
		board_obj.registerPlayer("akshay4");

		JSONObject temp1;
		JSONArray steps = new JSONArray();
		for (int position = 0; position <= 100; position++) {

			temp1 = new JSONObject("{\"number\":" + position + ",\"type\":" + 0 + ", \"target\":" + position + "}");
			steps.put(position, temp1);
		}

		for (int position = 1; position <= 6; position++) {

			temp1 = new JSONObject(
					"{\"number\":" + position + ",\"type\":" + 2 + ", \"target\":" + (position + 1) * 10 + "}");
			steps.put(position, temp1);
		}

		board_obj.data.put("steps", steps);

		JSONObject jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
				.get(Integer.parseInt(board_obj.data.get("turn").toString()));
	
		int initial_position = jsonobjofplayer_turn.getInt("position");

		for (int players = 0; players < 4; players++) {

			jsonobjofplayer_turn = (JSONObject) board_obj.data.getJSONArray("players")
					.get(Integer.parseInt(board_obj.data.get("turn").toString()));
			initial_position = jsonobjofplayer_turn.getInt("position");

			// roll dice
			JSONObject myjson = board_obj.rollDice(UUID.fromString(jsonobjofplayer_turn.get("uuid").toString()));

			int current_position = Integer.parseInt(myjson.get("dice").toString()) + initial_position;

			JSONObject temp = (JSONObject) board_obj.getData().getJSONArray("steps").get(current_position);

			int type = (Integer) temp.get("type");
		
			assertThat(type).isEqualTo(2);
			assertThat(myjson.get("message")).isEqualTo("Player climbed a ladder, moved to " + temp.getInt("target"));

		}

	}

	@Test
	public void check_snake()
			throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, JSONException, InvalidTurnException {
		Board board_obj = new Board();
		board_obj.registerPlayer("akshay1");
		board_obj.registerPlayer("akshay2");
		board_obj.registerPlayer("akshay3");
		board_obj.registerPlayer("akshay4");

		JSONObject temp1;
		JSONArray steps = new JSONArray();
		for (int position = 0; position <= 100; position++) {

			temp1 = new JSONObject("{\"number\":" + position + ",\"type\":" + 0 + ", \"target\":" + position + "}");
			steps.put(position, temp1);
		}

		for (int position = 51; position <= 56; position++) {

			temp1 = new JSONObject(
					"{\"number\":" + position + ",\"type\":" + 1 + ", \"target\":" + (position - 10) + "}");
			steps.put(position, temp1);
		}

		for (int playerindex = 0; playerindex <= 3; playerindex++) {
			board_obj.data.getJSONArray("players").getJSONObject(playerindex).put("position", 50);
		}

		board_obj.data.put("steps", steps);

		JSONObject object_of_turn_player = (JSONObject) board_obj.data.getJSONArray("players")
				.get(Integer.parseInt(board_obj.data.get("turn").toString()));
		JSONObject myjson;
		// System.err.println("here" + muy);

		int initial_position = object_of_turn_player.getInt("position"), type;

		// System.out.println("this " + myObj.data);

		for (int players = 0; players < 4; players++) {

			object_of_turn_player = (JSONObject) board_obj.data.getJSONArray("players")
					.get(Integer.parseInt(board_obj.data.get("turn").toString()));
			// System.err.println("here"+muy);
			initial_position = object_of_turn_player.getInt("position");

			// System.err.println("here then"+muy.getInt("position"));

			// roll dice
			myjson = board_obj.rollDice(UUID.fromString(object_of_turn_player.get("uuid").toString()));

			int current_position = Integer.parseInt(myjson.get("dice").toString()) + initial_position;

			JSONObject temp = (JSONObject) board_obj.getData().getJSONArray("steps").get(current_position);

			type = (Integer) temp.get("type");
			System.err.println("see for snake type in" + temp);

			assertThat(type).isEqualTo(1);

			assertThat(myjson.get("message"))
					.isEqualTo("Player was bit by a snake, moved back to " + temp.getInt("target"));

		}

	}

	@Test
	public void check_full_rolldice() throws FileNotFoundException, UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {

		Board myObj = new Board();

		myObj.registerPlayer("akshay1");
		myObj.registerPlayer("akshay2");
		myObj.registerPlayer("akshay3");
		myObj.registerPlayer("akshay4");

		JSONObject object_of_turn_player = (JSONObject) myObj.data.getJSONArray("players")
				.get(Integer.parseInt(myObj.data.get("turn").toString()));
		// System.err.println("here"+muy);
		int initial_position = object_of_turn_player.getInt("position");

		while (initial_position != 100) {

			object_of_turn_player = (JSONObject) myObj.data.getJSONArray("players")
					.get(Integer.parseInt(myObj.data.get("turn").toString()));
			// System.err.println("here"+muy);
			initial_position = object_of_turn_player.getInt("position");

			// System.err.println("here then"+muy.getInt("position"));

			// roll dice
			JSONObject input_json = myObj.rollDice(UUID.fromString(object_of_turn_player.get("uuid").toString()));

			int current_position = Integer.parseInt(input_json.get("dice").toString()) + initial_position;

			if(current_position<100)
			{
				
				System.out.println();
			JSONObject temp = (JSONObject) myObj.getData().getJSONArray("steps").get(current_position);
			int type = (Integer) temp.get("type");
			if (type == 2)
			// myObj.data.get("turn"))
			{

				assertThat(input_json.get("message"))
						.isEqualTo("Player climbed a ladder, moved to " + temp.getInt("target"));
				 System.err.println(object_of_turn_player+" Player climbed a ladder, moved to "+temp.getInt("target"));

			} else if (type == 0) {
				assertThat(input_json.get("message")).isEqualTo("Player moved to " + current_position);

				 System.err.println(object_of_turn_player+" Player moved to "+current_position);
			}

			else {
				assertThat(input_json.get("message"))
						.isEqualTo("Player was bit by a snake, moved back to " + temp.getInt("target"));
				 System.out.println(object_of_turn_player+" Player was bit by a snake, moved back to "+temp.getInt("target"));
			
			}

			assertThat(input_json.get("playerUuid")).isEqualTo(object_of_turn_player.get("uuid"));
			assertThat(input_json.get("playerName")).isEqualTo(object_of_turn_player.get("name"));
		}
		}
		System.out.println("Winner is "+object_of_turn_player.get("name"));
		
	}

}
