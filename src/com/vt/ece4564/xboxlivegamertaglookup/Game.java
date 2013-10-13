package com.vt.ece4564.xboxlivegamertaglookup;

public class Game {
	String nameOfGame_ = "";
	String datePlayed_ = "";
	
	public Game(){
		nameOfGame_ = "";
		datePlayed_ = "";
	}
	
	public Game(String gameName, String date){
		nameOfGame_ = gameName;
		datePlayed_ = date;
	}

	public String getNameOfGame() {
		return nameOfGame_;
	}
	public void setNameOfGame(String nameOfGame_) {
		this.nameOfGame_ = nameOfGame_;
	}
	
	public String getDatePlayed() {
		return datePlayed_;
	}
	public void setDatePlayed(String datePlayed_) {
		this.datePlayed_ = datePlayed_;
	}
}
