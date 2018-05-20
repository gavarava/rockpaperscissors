See all avaiable build tasks
gradlew.bat tasks

Build Using
gradlew.bat clean build

Run Unit Tests (This is not running the tests currently, but IDE can run all unit tests)
gradlew.bat test

The Projects Package Structure tries to apply "Clean Architecture" principles.
 

Some Requirements & Details 
A player is the "Call to Action" for this API. This is where the game will start.

SessionService.
	Create a Session when No Sessions Exist and a Player requests to join.
	A session could be Ready when only one player exists.
	Join a Session when another player requests.
	Create new session when there are no Ready Sessions.
	
A session contains Contains Players, two ready players make a game ?
An session is created when a Player requests a game.
	Player state initially is unready.
	Player state should be ready to start a game.
	Player Waits for a Game.
	
Someone takes ready players and randomly sets them up in a game ?	
What happens when multiple Unready players are there ?

Session
	Identified by a unique ID.
	Contains Two Players and State == Ready ....
	Contains a Game-ScoreCounter Map.

Player
	Requests a Game.
	Waits for the Game state to be Ready.
	Does an Action signifying the game play.
	Joins / Leaves the session.

Game
	Created when a player requests a game.
	Contains at least one player.
	State == Ready / Over / InProgress