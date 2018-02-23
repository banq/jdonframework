package sample.repository;

import java.util.HashMap;

import sample.domain.Match;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.OnEvent;
import com.jdon.annotation.pointcut.Around;

@Component
@Introduce("modelCache")
public class MatchRepositoryInMEM implements MatchRepository {

	private final HashMap<String, Match> memDB = new HashMap<String, Match>();

	@Around
	public Match find(String id) {
		return memDB.get(id);
	}

	@OnEvent("saveme")
	public void save(Match match) {
		memDB.put(match.getId(), match);
	}

	public HashMap<String, Match> getMemDB() {
		return memDB;
	}

}
