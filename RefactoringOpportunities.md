| Local        | Code smell          | Refactoring method                   | Nº aluno |
| ------------ | ------------------- | ------------------------------ | -------- |
| Tasks::taskD | Long Method         | Extract Method printTurnHeader | 121008   |
| Tasks::taskD | Long Method         | Extract Method printBoards     | 121008   |
| Tasks::taskD | Complex Conditional | Decompose Conditional          | 121008   |
| Tasks::taskD | Long Method         | Extract Method switchPlayers   | 121008   |
| Tasks::taskD | Long Method         | Extract Method printStats      | 121008   |

| Tasks::taskE | Long Method | Extract Method printTurnHeader | 110002 |
| Tasks::taskE | Long Method | Extract Method printBoards | 110002 |
| Tasks::taskE | Complex Conditional | Decompose Conditional | 110002 |
| Tasks::taskE | Long Method | Extract Method playTurn | 110002 |
| Tasks::taskE | Long Method | Extract Method checkWinner | 110002 |

| Board::printVisual | Duplicated Code | Extract Method printHeader | 108419 |
| Board::printVisual | Duplicated Code | Extract Method printRow | 108419 |
| Board::printVisual | Duplicated Code | Introduce Constant BORDER | 108419 |
| Board::printOpponentBoard | Complex Conditional | Extract Method isVisibleCell | 108419 |
| Board::printOpponentBoard | Duplicated Code | Extract Method printHeader | 108419 |

| Game::fire | Long Method | Extract Method validateShot | 110772 |
| Game::fire | Long Method | Extract Method handleHit | 110772 |
| Game::fire | Long Method | Extract Method handleMiss | 110772 |
| Game::fire | Complex Conditional | Decompose Conditional | 110772 |
| Game::fire | Multiple Responsibilities | Extract Method updateStats | 110772 |

