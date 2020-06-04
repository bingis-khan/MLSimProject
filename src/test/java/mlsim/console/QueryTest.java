package mlsim.console;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static mlsim.console.TestingUtils.randomSpacing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/* Thoughts: Too many useless tests? Focus more on tests as a contract or a use case, 
 *           less on edge cases. Maybe group tests by their purpose?
 */

@DisplayName("For a query")
public class QueryTest {
	private Query query;
	
	@Nested
	@DisplayName("when empty")
	public class GivenAnEmptyQuery {
		@BeforeEach
		public void setUpEmptyQuery() {
			query = new Query("");
		}
		
		@Test
		@DisplayName("calling isAtEnd() should return true.")
		public void whenCallingIsAtEnd_shouldReturnTrue() {
			assertTrue(query.isAtEnd(), 
					() -> "Empty query's isAtEnd() should return true.");
		}
		
		@Test
		@DisplayName("calling consume() should throw ParseException.")
		public void whenCallingConsume_shouldThrowParseException() {
			assertThrows(ParseException.class, () -> { 
				query.consume();
			});
		}
		
		@Test
		@DisplayName("calling next() should throw ParseException.")
		public void whenCallingNext_shouldThrowParseError() {
			assertThrows(ParseException.class, () -> { 
				query.next();
			});
		}
		
		@Test
		@DisplayName("calling consumeDouble() should throw ParseException.")
		public void whenCallingConsumeDouble_shouldThrowParseException() {
			assertThrows(ParseException.class, () -> { 
				query.consumeDouble();
			});
		}
		
		@Test
		@DisplayName("calling consumeInt() should throw ParseException.")
		public void whenCallingConsumeInt_shouldThrowParseException() {
			assertThrows(ParseException.class, () -> { 
				query.consumeInt();
			});
		}
	}
	
	/* Use inheritance or something to not repeat test?
	 * Because results should always be the same. */
	
	@Nested
	@DisplayName("when filled with data without trailing whitespace")
	class GivenAQueryFilledWithDataNoAdditionalWhitespace {
		
		@BeforeEach
		public void setup() {
			query = new Query(
					"burbon" + randomSpacing(5) +
					"krimbo" + randomSpacing(5) +
					42.1337 + randomSpacing(5) +
					"dingo");
		}
		
		@Nested
		@DisplayName("calling next()")
		class CallingNext {
			private String first;
			
			@BeforeEach
			public void setup() {
				first = query.next();
			}
			
			@Test
			@DisplayName("the first time should return the first whitespace-delimited string.")
			public void firstCallShouldReturnFirstString() {
				assertTrue(first.equals("burbon"));
			}
			
			@Test
			@DisplayName("the second time should return the second string.")
			public void secondCallShouldReturnSecondString() {
				String second = query.next();
				assertTrue(second.equals("krimbo"));
			}
			
			@Test
			@DisplayName("after running out of strings should throw ParseException.")
			public void afterStringsShouldThrowParseException() {
				query.next(); // second
				query.next(); // third
				query.next(); // fourth and last
				assertThrows(ParseException.class, () -> query.next());
			}
		}
		
		/* C O N S U M E */
		
		@Nested
		@DisplayName("calling consume()")
		class CallingConsume {
			
			@Test
			@DisplayName("with no parameters should not fail on any string.")
			public void withNoParametersShouldNotFail() {
				query.consume();
			}
			
			@Test
			@DisplayName("at the end of the query should throw ParseException.")
			public void atEndShouldThrowParseException() {
				query.consume(); // first
				query.consume(); // second
				query.consume(); // third
				query.consume(); // fourth
				assertThrows(ParseException.class, () -> query.consume());
			}
			
			@Test
			@DisplayName("with matching first parameter should not fail.")
			public void withMatchingFirstParameterShouldNotFail() {
				query.consume("burbon");
			}
			
			@Test
			@DisplayName("with matching second parameter should not fail.")
			public void withMatchingSecondParameterShouldNotFail() {
				query.consume("ayyyyyyyyy", "burbon");
			}
			
			@Test
			@DisplayName("with no matching parameters should throw ParseException.")
			public void withNoMatchingParametersShouldThrowParseException() {
				assertThrows(ParseException.class, () -> query.consume("ayyyyyyyyy", "17764201337fucc"));
			}
			
			@Test
			@DisplayName("the second time with matching parameters should not fail.")
			public void secondTimeWithMatchingParameters() {
				query.consume("burbon");
				query.consume("krimbo");
			}
			
			@Test
			@DisplayName("should return the matched string.")
			public void shouldReturnMatchedString() {
				assertEquals(query.consume("burbon", "amadeusz"), "burbon");
			}
		}
		
		@Test
		@DisplayName("calling consumeInt() on a string should throw ParseException.")
		public void consumeIntOnStringShouldThrowParseException() {
			assertThrows(ParseException.class, () -> query.consumeInt());
		}
		
		@Test
		@DisplayName("consumeDouble() on a string should throw ParseException.")
		public void consumeDoubleOnStringShouldThrowParseException() {
			assertThrows(ParseException.class, () -> query.consumeDouble());
		}
		
		@Test
		@DisplayName("calling isAtEnd() should only be true after the end of the string.")
		public void callingIsAtEndShouldOnlyBeTrueAtEnd() {
			assertFalse(query.isAtEnd()); // first
			query.consume();
			
			assertFalse(query.isAtEnd()); // second
			query.consume();
			
			assertFalse(query.isAtEnd()); // third
			query.consume();
			
			assertFalse(query.isAtEnd()); // fourth
			query.consume();
			
			assertTrue(query.isAtEnd()); // no more strings left
		}
		
	}
	
	/* 
	 * These classes both share most of their tests with 
	 * "GivenAQueryFilledWithDataNoAdditionalWhitespace" (bleh)
	 *  thus the use of inheritance. Only the setup method must 
	 *  be overridden to provide different testing conditions. 
	 *  
	 *  NOTE: Since hard-coded strings are used instead of, for example, 
	 *  a list, same words in the same order must be provided. 
	 */
	
	@Nested
	@DisplayName("when filled with data with trailing whitespace")
	class GivenAQueryFilledWithDataWithTrailingWhitespace 
		extends GivenAQueryFilledWithDataNoAdditionalWhitespace {
		@BeforeEach
		@Override
		public void setup() {
			query = new Query(
					"burbon" + randomSpacing(5) +
					"krimbo" + randomSpacing(5) +
					42.1337 + randomSpacing(5) +
					"dingo" + randomSpacing(5));
		}
		
	}
	
	@Nested
	@DisplayName("when filled with data with leading whitespace")
	class GivenAQueryFilledWithDataWithLeadingWhitespace 
		extends GivenAQueryFilledWithDataNoAdditionalWhitespace {
		@BeforeEach
		@Override
		public void setup() {
			query = new Query(randomSpacing(5) +
					"burbon" + randomSpacing(5) +
					"krimbo" + randomSpacing(5) +
					42.1337 + randomSpacing(5) +
					"dingo" + randomSpacing(5));
		}
	}
	
	@Nested
	@DisplayName("filled with integers")
	class GivenAQueryWithIntegers {
		
		@BeforeEach
		public void setup() {
			query = new Query(69 + randomSpacing(5) + 420);
		}
		
		@Test
		@DisplayName("calling consumeInt() should return the first integer.")
		public void callingConsumeIntShouldReturnFirstInteger() {
			assertEquals(query.consumeInt(), 69);
		}
		
		@Test
		@DisplayName("calling consumeInt() the second time should return the second integer.")
		public void callingConsumeIntSecondTimeShouldReturnSecondInteger() {
			query.consumeInt();
			assertEquals(query.consumeInt(), 420);
		}
		
		@Test
		@DisplayName("calling consumeDouble() should return the first integer (as double).")
		public void callingConsumeDoubleShouldReturnFirstInteger() {
			assertEquals(query.consumeDouble(), (double)69);
		}
		
		/* Useless? */
		
		@Test
		@DisplayName("calling next() should return first integer as string")
		public void callingNextShouldReturnFirstIntegerAsString() {
			assertEquals(query.next(), ""+69);
		}
		
		@Test
		@DisplayName("calling consume() with appropriate parameters should not fail.")
		public void callingConsumeWithMatchingParametersShouldNotFail() {
			query.consume(""+69);
		}
	}
	
	@Nested
	@DisplayName("filled with doubles")
	class GivenAQueryWithDoubles {
		
		@BeforeEach
		public void setup() {
			query = new Query(69.1337 + randomSpacing(5) +
								420.1488);
		}
		
		@Test
		@DisplayName("calling consumeInt() should throw ParseException.")
		public void callingConsumeIntShouldThrowParseException() {
			assertThrows(ParseException.class, () -> query.consumeInt());
		}
		
		@Test
		@DisplayName("calling consumeDouble() should return the first double.")
		public void callingConsumeDoubleShouldReturnFirstDouble() {
			assertEquals(query.consumeDouble(), 69.1337);
		}
		
		@Test
		@DisplayName("calling consumeDouble() the second time should return the second double.")
		public void callingConsumeDoubleShouldReturnSecondDouble() {
			query.consumeDouble();
			assertEquals(query.consumeDouble(), 420.1488);
		}
		
		/* Useless? */
		
		@Test
		@DisplayName("calling next() should return first double as string")
		public void callingNextShouldReturnFirstDoubleAsString() {
			assertEquals(query.next(), ""+69.1337);
		}
		
		@Test
		@DisplayName("calling consume() with matching parameter should not fail.")
		public void callingConsumeWithMatchingParametersShouldNotFail() {
			query.consume(""+69.1337);
		}
	}
	
	@Test
	@DisplayName("calling throwError() actually throws an error (exception to be exact.)")
	public void callingThrowErrorThrowsException() {
		query = new Query("");
		assertThrows(ParseException.class, () -> query.throwError(""));
	}

}
