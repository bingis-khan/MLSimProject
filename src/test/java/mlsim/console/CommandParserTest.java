package mlsim.console;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A CommandParser")
public class CommandParserTest {
	private ConsoleApp console;
	private CommandParser parser;
	private Commands commands;
	
	@Test
	@DisplayName("when calling parse(), a command should receive a query without the first string (effectively its name).")
	public void whenCallingParse_commandShouldReceiveQueryWithoutFirstString() {
		commands = new Commands(new Command("pc usage", "pc desc", this::queryUse, "ali"));
		
		parser = new CommandParser(commands);
		console = new ConsoleApp(parser);
		parser.parse("ali baba", console);
	}
	
	/* Property of the above test. */
	public void queryUse(Query userQuery, ConsoleApp context) {
		assertEquals(userQuery.next(), "baba");
	}
	
	
	@Test
	@DisplayName("calling parse() should not throw an exception.")
	public void whenCallingParse_shouldNotThrowException() {
		commands = new Commands(new Command("throws usage", "throws usage", this::exceptional, "thr"));
		
		parser = new CommandParser(commands);
		console = new ConsoleApp(parser);
		
		parser.parse("thr", console);
	}
	
	/* Used for above test. */
	public void exceptional(Query q, ConsoleApp context) {
		q.throwError("dupa");
	}
	
	@Test
	@DisplayName("calling parse() with an empty string should return normally (NOT throw an exception).")
	public void whenClassingParseWithEmptyString_shouldNotThrowException() {
		parser = new CommandParser(commands);
		console = new ConsoleApp(parser);
		
		parser.parse("", console);
	}
}
