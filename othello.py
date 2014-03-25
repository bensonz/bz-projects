# Othello

def make2dList(rows, cols):
    a=[]
    for row in xrange(rows): a += [[0]*cols]
    return a

def hasMove(board, player):
    (rows, cols) = (len(board), len(board[0]))
    for row in xrange(rows):
        for col in xrange(cols):
            if (hasMoveFromCell(board, player, row, col)):
                return True
    return False

def hasMoveFromCell(board, player, startRow, startCol):
    (rows, cols) = (len(board), len(board[0]))
    if (board[startRow][startCol] != 0):
        return False
    for dir in xrange(8):
        if (hasMoveFromCellInDirection(board, player, startRow, startCol, dir)):
            return True
    return False

def hasMoveFromCellInDirection(board, player, startRow, startCol, dir):
    (rows, cols) = (len(board), len(board[0]))
    dirs = [ (-1, -1), (-1, 0), (-1, +1),
             ( 0, -1),          ( 0, +1),
             (+1, -1), (+1, 0), (+1, +1) ]
    (drow,dcol) = dirs[dir]
    i = 1
    while True:
        row = startRow + i*drow
        col = startCol + i*dcol
        #if ((row < 0): or (row >= rows) or (col < 0) or (col >= cols)):
        """if(row<=-1):
            row = row + rows -1
        elif(row>=rows):
            row = row - rows -1
        if(col<=-1):
            col = col + cols - 1
        elif(col>=cols):
            col = col - cols - 1"""
        row = row % rows
        col = col % cols
        if (board[row][col] == 0):
            # no blanks allowed in a sandwich!
            return False
        elif (board[row][col] == player):
            # we found the other side of the 'sandwich'
            break
        else:
            # we found more 'meat' in the sandwich
            i += 1
    return (i > 1)

def makeMove(board, player, startRow, startCol):
    # assumes the player has a legal move from this cell
    (rows, cols) = (len(board), len(board[0]))
    for dir in xrange(8):
        if (hasMoveFromCellInDirection(board, player, startRow, startCol, dir)):
            makeMoveInDirection(board, player, startRow, startCol, dir)
    board[startRow][startCol] = player

def makeMoveInDirection(board, player, startRow, startCol, dir):
    (rows, cols) = (len(board), len(board[0]))
    dirs = [ (-1, -1), (-1, 0), (-1, +1),
             ( 0, -1),          ( 0, +1),
             (+1, -1), (+1, 0), (+1, +1) ]
    (drow,dcol) = dirs[dir]
    i = 1
    while True:
        row = startRow + i*drow
        col = startCol + i*dcol
        row = row % rows
        col = col % cols
        if (board[row][col] == player):
            # we found the other side of the 'sandwich'
            break
        else:
            # we found more 'meat' in the sandwich, so flip it!
            board[row][col] = player
            i += 1

def getPlayerLabel(player):
    labels = ["-", "X", "O"]
    return labels[player]

def printColLabels(board):
    (rows, cols) = (len(board), len(board[0]))
    print "  ", # skip row label
    for col in xrange(cols): print chr(ord("A")+col),
    print

def printBoard(board,player):
    (rows, cols) = (len(board), len(board[0]))
    printColLabels(board)
    for row in xrange(rows):
        print "%2d" % (row+1),
        for col in xrange(cols):
            if isLegalMove(board,player,row,col): print ("."),
            else: print getPlayerLabel(board[row][col]),
        print "%2d" % (row+1)
    printColLabels(board)

def isLegalMove(board, player, row, col):
    (rows, cols) = (len(board), len(board[0]))
    if ((row < 0) or (row >= rows) or (col < 0) or (col >= cols)): return False
    return hasMoveFromCell(board, player, row, col)


def getMove(board, player):
    print "\n**************************"
    printBoard(board,player)
    while True:
        if player == 1:
            prompt = "Enter move for player " + getPlayerLabel(player) + ": "
            move = raw_input(prompt).upper()
            # move is something like "A3"
            #if ((len(move) != 2) or (not move[0].isalpha()) or (not move[1].isdigit())):
#                print "Wrong format!  Enter something like A3 or D5."
            if len(move) <= 2 or not move[0].isalpha() or len(move) > 3:
                print "Wrong format! Enter something like A2 or D3"
            elif len(move) == 2:
                col = ord(move[0]) - ord('A')
                row = int(move[1])-1
            elif len(move) == 3:
                col = ord(move[0]) - ord('A')
                row = eval(move[1:3])-1
            if (not isLegalMove(board, player, row, col)):
                print "That is not a legal move!  Try again."
            else:
                return (row, col)
        else:
            move = playOthelloAgainstRandomComputer(board)
            col = move[0]
            row = move[1]
            return (row,col)
        
def getLegalMoves(board, player):
    (rows, cols) = (len(board), len(board[0]))
    list_moves = []
    for row in xrange(rows):
        for col in xrange(cols):
            if isLegalMove(board, player, row, col) == True:
                list_moves += [(row, col)]
    return list_moves

def playOthello(rows, cols):
    # create initial board
    board = make2dList(rows, cols)
    board[rows/2][cols/2] = board[rows/2-1][cols/2-1] = 1
    board[rows/2-1][cols/2] = board[rows/2][cols/2-1] = 2
    (currentPlayer, otherPlayer) = (1, 2)
    # and play until the game is over
    while True:
        print getLegalMoves(board,currentPlayer)
        if (hasMove(board, currentPlayer) == False):
            if (hasMove(board, otherPlayer)):
                print "No legal move!  PASS!"
                (currentPlayer, otherPlayer) = (otherPlayer, currentPlayer)
            else:
                print "No more legal moves for either player!  Game over!"
                break
        print currentScore(board,currentPlayer,otherPlayer)
        (row, col) = getMove(board, currentPlayer)
        makeMove(board, currentPlayer, row, col)
        (currentPlayer, otherPlayer) = (otherPlayer, currentPlayer)
    print "Goodbye!"

def currentScore(board, currentPlayer, OtherPlayer):
    (rows, cols) = (len(board), len(board[0]))
    CurrentPlayerScore = 0
    OtherPlayerScore = 0
    for row in xrange(rows):
        for col in xrange(cols):
            if board[row][col] == currentPlayer:
                CurrentPlayerScore += 1
            elif board[row][col] == OtherPlayer:
                OtherPlayerScore += 1
    print  "Scores for player%s is %2d" % (getPlayerLabel(currentPlayer), CurrentPlayerScore)
    return "Scores for player%s is % 2d" % (getPlayerLabel(OtherPlayer),OtherPlayerScore)

import random

def playOthelloAgainstRandomComputer(board):
   #(rows, cols) = (len(board), len(board[0]))
    player = 1
    moveList = getLegalMoves(board, player)
    print moveList
    move = random.randint(0,len(moveList)-1)
    move = moveList[move]
    print "move",move
    return move
        
    

playOthello(9,9)
