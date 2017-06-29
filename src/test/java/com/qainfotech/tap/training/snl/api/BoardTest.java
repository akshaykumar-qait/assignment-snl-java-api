package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.qainfotech.tap.training.snl.api.Board;
import com.qainfotech.tap.training.snl.api.GameInProgressException;
import com.qainfotech.tap.training.snl.api.MaxPlayersReachedExeption;
import com.qainfotech.tap.training.snl.api.PlayerExistsException;

import static org.assertj.core.api.Assertions.*;

public class BoardTest {

	Board boardreader,boardreader1;

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
		//System.err.println((boardreader.registerPlayer("shadab5")).length());
		
		assertThat((boardreader.registerPlayer("shadab")).length()).isEqualTo(1);
		assertThat((boardreader.registerPlayer("shadab1")).length()).isEqualTo(2);

		JSONObject hey = (JSONObject) boardreader.data.getJSONArray("players").get(1);

		UUID my = UUID.fromString(hey.get("uuid").toString());
		assertThat(boardreader.deletePlayer(my).length()).isEqualTo(1);

	}


	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
	public void if_playerlist_exceeds_the_number_four()
			throws UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption{

		boardreader1 = new Board();
	    boardreader1.registerPlayer("shadab1");
		boardreader1.registerPlayer("shadab2");
		boardreader1.registerPlayer("shadab3");
		boardreader1.registerPlayer("shadab4");
		boardreader1.registerPlayer("shadab5");
	}

	@Test(expectedExceptions = PlayerExistsException.class, expectedExceptionsMessageRegExp = "Player 'shadab' already exists on board")
	public void if_entered_same_player_name_should_return_exception()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {

		boardreader = new Board();
		boardreader.registerPlayer("shadab");
		boardreader.registerPlayer("shadab");
		
	}
	
	@Test
	public  void check_full_rolldice() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException
	{
		
		Board myObj = new Board();
		
		myObj.registerPlayer("akshay1");
		myObj.registerPlayer("akshay2");
		myObj.registerPlayer("akshay3");
		myObj.registerPlayer("akshay4");
//		
//		UUID myarray[] = new UUID[4];
//	
//		JSONArray array = myObj.data.getJSONArray("players");
//		JSONObject tmp;
//		for(int a=0;a<array.length();a++)
//		{
//			tmp = array.getJSONObject(a);
//			myarray[a]=(UUID) tmp.get("uuid");
//			System.out.println(myarray[a]);
//
//		}
		
		
		for(int a=0;a<=20;a++)
		{
		
		JSONObject muy= (JSONObject) myObj.data.getJSONArray("players").get(Integer.parseInt(myObj.data.get("turn").toString()));
		System.err.println("here"+muy);
		int initial_position=muy.getInt("position");
		

		System.err.println("here then"+muy.getInt("position"));
		
		// roll dice
		JSONObject  myjson = myObj.rollDice(UUID.fromString(muy.get("uuid").toString()));
		
		int current_position=Integer.parseInt(myjson.get("dice").toString())+initial_position;
		
		JSONObject temp =(JSONObject) myObj.getData().getJSONArray("steps").get(current_position);
		int type =  (Integer) temp.get("type");
		if( type==2)
		//		myObj.data.get("turn"))
		{
		
			
			assertThat(myjson.get("message")).isEqualTo("Player climbed a ladder, moved to "+temp.getInt("target"));
		System.err.println("Player climbed a ladder, moved to "+temp.getInt("target"));
		
		}
		else if(type==0)
		{
		assertThat(myjson.get("message")).isEqualTo("Player moved to "+current_position);
		
		System.out.println("Player moved to "+current_position);
		}
		
		else
		{
			assertThat(myjson.get("message")).isEqualTo("Player was bit by a snake, moved back to "+temp.getInt("target"));
			System.out.println("Player was bit by a snake, moved back to "+temp.getInt("target"));
		}

		assertThat(myjson.get("playerUuid")).isEqualTo(muy.get("uuid"));
		assertThat(myjson.get("playerName")).isEqualTo(muy.get("name"));
		}
		
		
		
	}

}
