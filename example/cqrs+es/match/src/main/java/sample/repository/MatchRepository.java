package sample.repository;

import java.util.HashMap;

import sample.domain.Match;

public interface MatchRepository {
	Match find(String id);

	void save(Match match);

	// for test this is query directly from db.
	public HashMap<String, Match> getMemDB();
}
