# Tic Tac Toe

- This is my implementation of tic tac toe game. There are two ways to invoke
    - Console
    - UI
    
- The UI is done through JavaFx. As of the current version, 
    - the reset functionality is a TODO.
    - the java FX app class fields are all static. This needs to be researched more on how to fix.

- mvn is used but there are no external dependencies.

- Steps to execute
    - For UI: `mvn exec:java -Dexec.mainClass="edu.jyo.practice.tictactoe.main.Main"`
    - For console only: `mvn exec:java -Dexec.mainClass="edu.jyo.practice.tictactoe.main.Main" -Dexec.args="console"`
