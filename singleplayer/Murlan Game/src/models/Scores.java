package models;
import java.util.ArrayList;
import java.util.List;

import players.Player;

import java.util.HashMap;

public class Scores 
{
	private List<Player> players;
	private ArrayList<HashMap<Player, Integer>> scores;
	private Player lastRoundWinner;
	private Player lastRoundLoser;
	
	public Scores(List<Player> players)
	{
		this.players = players;
		scores = new ArrayList<HashMap<Player, Integer>>();
	}
	//if someone has reached 21 points, game will be over
	public boolean isGameOver()
	{
		int[] sums = {0, 0, 0, 0};
		for(int i = 0; i < scores.size(); i++)
		{
			for(Player player : players)
			{
				sums[players.indexOf(player)] = sums[players.indexOf(player)] + scores.get(i).get(player);
			}
		}
		
		int cnt = 0;
		for(int i = 0; i < 4; i++)
		{
			if(sums[i] >= 21)
				cnt++;
		}
		if(cnt == 1)
			return true;
		else
			return false;
	}
	
	public void updateRoundScores(HashMap<Player, Integer> positions)
	{
		scores.add(positions);
		
		for(Player player : players)
		{
			if(positions.get(player) == 3)
				lastRoundWinner = player;
			else if(positions.get(player) == 0)
				lastRoundLoser = player;
		}
	}
	
	public List<Player> getPlayers()
	{
		return players;
	}
	
	public ArrayList<HashMap<Player, Integer>> getScores()
	{
		return scores;
	}
	
	public Player lastRoundWinner()
	{
		return lastRoundWinner;
	}
	
	public Player lastRoundLoser()
	{
		return lastRoundLoser;
	}
}
