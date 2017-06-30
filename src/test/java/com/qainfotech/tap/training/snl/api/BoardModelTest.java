package com.qainfotech.tap.training.snl.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.json.simple.parser.JSONParser;


public class BoardModelTest {

	BoardModel boardmodelreader;

	@BeforeTest
	public void makeobject() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		boardmodelreader = new BoardModel();
	}
	
	
	@Test
	public void Acheck_save_with_data() throws IOException
	{
		boardmodelreader = new BoardModel();
		UUID uuid = UUID.randomUUID();
		
		JSONObject temp_data = new JSONObject() ;
		temp_data.put("test", "tested");
		
		
		System.out.println(temp_data);
		boardmodelreader.save(uuid,temp_data);
		
		assertThat(boardmodelreader.data(uuid).toString()).isEqualTo(temp_data.toString());
		
	}
	
	
	@Test
	public void check_init() throws JSONException, IOException
	{
		boardmodelreader = new BoardModel();
	UUID uuid = UUID.randomUUID();
		boardmodelreader.init(uuid);
		
		
		JSONObject input = new JSONObject(new String(Files.readAllBytes(Paths.get(uuid.toString() + ".board"))));
		
		assertThat(input.get("turn")).isEqualTo(0);
		assertThat(input.getJSONArray("players").length()).isEqualTo(0);
		assertThat(input.getJSONArray("steps").length()).isEqualTo(101);
		
		
		
		assertThat(input.getJSONArray("steps").getJSONObject(0).get("number")).isEqualTo(0);
		assertThat(input.getJSONArray("steps").getJSONObject(0).get("type")).isEqualTo(0);
		assertThat(input.getJSONArray("steps").getJSONObject(0).get("target")).isEqualTo(0);
		
		
		
		
	}
	
	
	
	
	

}
