from Tkinter import *
import random

def redrawAll(canvas):
    if canvas.data.isGameOver == False:
        canvas.delete(ALL)
        drawGame(canvas)
        drawFallingPiece(canvas)
        removeFullRows(canvas)
        drawScores(canvas)
    if canvas.data.isGameOver== True:
        #Game over, shows final score
        text = "Game Over. Final score: %d" % canvas.data.score
        canvas.create_text(canvas.data.Width/2,\
                           canvas.data.Height/2, text = text,\
                           font = ("Helvetica",40))
        
def init(canvas):
    #initialize the settings
    canvas.data.isGameOver = False
    canvas.data.score = 0
    canvas.data.fullRow = 0
    initializeBoard(canvas)
    newFallingPiece(canvas)
    redrawAll(canvas)

def drawScores(canvas):
    margin = 50
    text = "Your current score : %d" % canvas.data.score
    canvas.create_text(canvas.data.Width/2,margin/2,text= text,\
                       font = ("Helvetica",30))
    
def initializeBoard(canvas):
    # gives out a 2 d list with all elements being "blue"
    rows,cols,cellSize,margin = 15,10,40,50
    board = []
    for row in xrange(rows):
        curRow = []
        for col in xrange(cols):
            curRow += ["blue"]
        board += [curRow]
    canvas.data.board = board

def removeFullRows(canvas):
    board = canvas.data.board
    newRow = []
    fullRow = canvas.data.fullRow
    for row in xrange(14,-1,-1): # from bottom up
        if ("blue" in board[row]):# not filled yet, make it still as what it is
            newRow += [board[row]]
        else:# filled, disregard the full row, full row numbers +1,
            fullRow += 1
    for row in xrange(fullRow):
        # how many full rows are cleared = how many new rows need to be added
        curRow = []
        for col in xrange(10):
            curRow += ["blue"]
        newRow += [curRow]
    printBoard = []
    for row in xrange(len(newRow)-1,-1,-1):
        printBoard += [newRow[row]]
    canvas.data.board = printBoard
    canvas.data.score += (fullRow**2)


def drawGame(canvas):
    # draw the background and the game
    board = canvas.data.board
    rows,cols= 15,10
    canvas.create_rectangle(0,0,canvas.data.Width,canvas.data.Height,\
                            fill="orange")
    for row in xrange(rows):
        for col in xrange(cols):
            drawCell(canvas, row, col, board[row][col])

def moveFallingPiece(canvas, drow, dcol):
    canvas.data.drow = drow
    canvas.data.dcol = dcol
    canvas.data.PosRow += drow
    canvas.data.PosCol += dcol
    if not(isLegalMove(canvas, canvas.data.curFall)):
        canvas.data.PosRow -= drow
        canvas.data.PosCol -= dcol
        return False
    else:
        return True

def rotateFallingPiece(canvas):
    oldPiece = canvas.data.curFall
    oldRow = canvas.data.PosRow
    oldCol = canvas.data.PosCol
    oldDimension = (len(oldPiece),len(oldPiece[0]))
    oldCenterRow, oldCenterCol = fallingPieceCenter(canvas,oldRow,oldCol,\
                                                     oldDimension)
    newDimension = (len(oldPiece[0]),len(oldPiece))
    newCenterRow, newCenterCol = fallingPieceCenter(canvas,oldRow,oldCol,\
                                                    newDimension)
    newCenterRow = oldCenterRow - newCenterRow + oldRow
    newCenterCol = oldCenterCol - newCenterCol + oldCol
    output = []
    for row in xrange(newDimension[0]):
        newRow = []
        for col in xrange(newDimension[1]-1,-1,-1):
            newRow += [oldPiece[col][row]]
        output += [newRow]
    canvas.data.curFall = output
    if isLegalMove(canvas,output) == False: canvas.data.curFall = oldPiece

def fallingPieceCenter(canvas,row,col,dim):
    # find the piece center -- where the piece is going to
    # rotate about
    cr = row + dim[0]/2
    cc = col + dim[1]/2
    return (cr,cc)
    

def drawCell(canvas,row,col,color):
    # display the 2d list as a board with pieces
    cellSize,margin = 40,50
    left = margin + cellSize*col
    top = margin + cellSize*row
    right = margin + cellSize*(col+1)
    botton = margin + cellSize*(row+1)
    canvas.create_rectangle(left,top,right,botton, width= 7, fill = color)

def newFallingPiece(canvas):
    canvas.data.Pieces = [ iPiece(canvas), jPiece(canvas), lPiece(canvas),
                                 oPiece(canvas), sPiece(canvas), tPiece(canvas),
                                 zPiece(canvas) ]
    canvas.data.PieceColors = [ "red", "yellow", "magenta", "pink",
                                      "cyan", "green", "orange" ]
    piece = random.randint(0,6)
    # generate a new falling piece randomly
    fallingPiece = canvas.data.curFall = canvas.data.Pieces[piece]
    pieceColor = canvas.data.curColor = canvas.data.PieceColors[piece]
    PosCol = canvas.data.PosCol = 4 # starting column is 4, the middle
    PosRow = canvas.data.PosRow = 0 # starting row is the first row


def drawFallingPiece(canvas):
    pieceColor = canvas.data.curColor
    PosCol = canvas.data.PosCol
    PosRow = canvas.data.PosRow
    for pieceRow in xrange(len(canvas.data.curFall)):
        for pieceCol in xrange(len(canvas.data.curFall[0])):
            if canvas.data.curFall[pieceRow][pieceCol] == True:
                drawCell(canvas,PosRow+pieceRow,PosCol+pieceCol,pieceColor)
    
def isLegalMove(canvas, fallingPiece):
    # check if leagal
    board = canvas.data.board
    checkRow = canvas.data.PosRow
    for row in xrange(len(fallingPiece)):
        for col in xrange(len(fallingPiece[0])):
            checkCol = canvas.data.PosCol
            if fallingPiece[row][col] == True:
                checkCol += col
                #check if in bound
                if checkRow > 14 or checkCol < 0 or checkCol > 9:
                    return False
                else:
                    #check if collide with any piece
                    if board[checkRow][checkCol] != "blue":
                        return False
        checkRow += 1
    return True

def placePieces(canvas):
    # set the piece to the 2d list after it is not legal to move anymore
    board = canvas.data.board
    PosCol = canvas.data.PosCol
    PosRow = canvas.data.PosRow
    curPiece = canvas.data.curFall
    color = canvas.data.curColor
    for i in xrange(len(canvas.data.curFall)):
        for j in xrange(len(canvas.data.curFall[0])):
            if curPiece[i][j] == True:
                board[PosRow+i][PosCol+j] = color


def keyPressed(canvas,event):
    if event.char == "q":
        canvas.data.isGameOver = True
    elif event.char == "r":
        init(canvas)
    if canvas.data.isGameOver == False:
        # only if the game is still on are you able to move piece
        if isLegalMove(canvas, canvas.data.curFall):
            if event.keysym == "Left":
                moveFallingPiece(canvas,0,-1)
            elif event.keysym == "Right":
                moveFallingPiece(canvas,0,1)
            elif event.keysym == "Up":
                rotateFallingPiece(canvas)
            elif event.keysym == "Down":
                moveFallingPiece(canvas,1,0)
    redrawAll(canvas) # but redraw all either way
            

def timerFired(canvas):
    if canvas.data.isGameOver == False:
        # game still on, move the piece
        moveFallingPiece(canvas,+1,0)
        redrawAll(canvas)
    if canvas.data.isGameOver == True:
        redrawAll(canvas)
    if isLegalMove(canvas,canvas.data.curFall) == False:
        # not legal move, game over
        canvas.data.isGameOver = True
    elif moveFallingPiece(canvas,+1,0) == False:
        # moving piece still can move, keep it moving
        placePieces(canvas)
        newFallingPiece(canvas)
    delay = 500 # 500 ms is a good playable time
    canvas.after(delay, timerFired,canvas)

def run():
    root = Tk()
    rows,cols,cellSize,margin = 15,10,40,50 # default settings to the widget
    Width = 2*margin + cols * cellSize
    Height =  2*margin + rows*cellSize
    canvas = Canvas(root, width =Width, height = Height)
    canvas.pack()
    root.resizable(width=0, height=0)
    class Struct: pass
    canvas.data = Struct()
    canvas.data.Width = Width
    canvas.data.Height = Height
    root.canvas = canvas.canvas = canvas
    root.bind("<Key>", lambda event: keyPressed(canvas,event))
    init(canvas)
    timerFired(canvas)
    root.mainloop()


def iPiece(canvas):
    return [[True,True,True,True]]

def jPiece(canvas):
    return [[True, False,False],
            [True,True,True]]

def lPiece(canvas):
    return [[False,False,True],
            [True,True,True]]

def oPiece(canvas):
    return [
    [ True, True],
    [ True, True]
    ]
    
def sPiece(canvas):
    return [
    [ False, True, True],
    [ True,  True, False ]
    ]
    
def tPiece(canvas):
    return [
    [ False, True, False ],
    [ True,  True, True]
    ]
    
def zPiece(canvas):
    return [
    [ True,  True, False ],
    [ False, True, True]
    ]

run()
