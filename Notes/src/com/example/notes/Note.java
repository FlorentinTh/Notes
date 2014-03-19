package com.example.notes;

public class Note {
	private Integer id;
	private String titre;
	private String content;
	private String password;
	
	public Note(){}
	
	public Note(Integer id, String titre, String content, String password) {
		this.id = id;
		this.titre = titre;
		this.content = content;
		this.password = password;
	}
	
	public Note(String titre, String content, String password) {
		this.titre = titre;
		this.content = content;
		this.password = password;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
