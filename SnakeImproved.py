from Tkinter import *
import random

def init(canvas):
    # initialize everything
    printInstructions()
    canvas.data.score = 0
    loadSnakeBoard(canvas)
    canvas.data.wallsRuninto = 0
    canvas.data.moves = 0
    canvas.data.bonus = 0
    canvas.data.isPause = False
    canvas.data.inDebugMode = False
    canvas.data.isGameOver = False
    canvas.data.snakeDrow = -1
    canvas.data.snakeDcol = 0
    canvas.data.ignoreNextTimerEvent = False
    canvas.data.colorTuple = ("blue","green","red")
    # starting drawing!
    redrawAll(canvas)
    
def loadSnakeBoard(canvas):
    rows, cols = canvas.data.rows, canvas.data.cols
    snakeBoard = []
    for row in xrange(rows):
        rowList = []
        for col in xrange(cols): # a snake board with all the 0s
            rowList += [0]
        snakeBoard += [rowList]
    snakeBoard[rows/2][cols/2] = 1
    canvas.data.board = snakeBoard
    # got the board
    findSnakeHead(canvas) # find head
    placeFoodAndPosion(canvas)# put down food, game could start now!
    
def printInstructions():
    print "This is MORE snake made by benson!"
    print "Same as always, eat food to grow, try not to hit the wall/yourself"
    print "Press 'd' for debug mode"
    print "Press 'r' to restart"
    print "Press 'p' to pause, press 'p' again to resume"
    print "While in pause, you may draw walls! "
    print "Running into walls doesn't kill you, but if you survive 20 moves,"
    print "You get one point bonus automatically!"

def timerFired(canvas):
    ignoreThisTimerEvent = canvas.data.ignoreNextTimerEvent
    canvas.data.ignoreNextTimerEvent = False
    if canvas.data.isGameOver == False and ignoreThisTimerEvent == False and \
       canvas.data.isPause == False:
            drow = canvas.data.snakeDrow
            dcol = canvas.data.snakeDcol
            canvas.data.colorTuple = ("blue","green","red")
            # set color back to normal after un-pause
            moveSnake(canvas,drow,dcol)
            redrawAll(canvas)
            if canvas.data.score < 3:
                # only count moves in level one
                canvas.data.moves += 1
    if canvas.data.score < 3:
        # level 1 speed
        delay = 100
    else:
        # level 2 speed, This is definitely play-able
        delay = 50
    canvas.after(delay,timerFired,canvas)


def redrawAll(canvas):
    canvas.delete(ALL)
    drawSnakeBoard(canvas)
    drawScore(canvas)
    findScore(canvas)
    if canvas.data.score < 0 :
        canvas.data.isGameOver = True
    if canvas.data.isGameOver == True:
        x = canvas.data.Width/2
        y = canvas.data.Height/2
        textSize = canvas.data.Width/17
        highScore = findHighScores(canvas)
        highScore = highScore[0:3]
        text = "Game over! Your high scores are: \n"
        canvas.create_text(x,y,text=text,font=("Helvetica",textSize,"bold"))
        canvas.create_text(x,y+textSize,text = highScore, font\
                           = ("Helvetica",textSize,"bold"))

def drawScore(canvas):
    text = "Your current score : %d" % canvas.data.score
    x = canvas.data.Width/2
    y = 25 #margin/2
    canvas.create_text(x,y,text = text, font = ("Helvetica",20,"bold"))

def drawSnakeBoard(canvas):
    board = canvas.data.board
    rows, cols = len(board),len(board[0])
    for row in range(rows):
        for col in range(cols):
            drawSnakeCell(canvas,board, row, col)
            
def drawSnakeCell(canvas,board, row, col):
    margin = 50
    cellSize = 30
    left = margin + col * cellSize
    right = left + cellSize
    top = margin + row * cellSize
    bottom = top + cellSize
    canvas.create_rectangle(left, top, right, bottom,width = 3, fill="white")
    #Function Too long, a helper function is written
    drawSnakeCell2(canvas,board,margin,cellSize,row,col,left,right,top,bottom)
    # for debugging, draw the number in the cell
    if (canvas.data.inDebugMode == True):
        canvas.create_text(left+cellSize/2,top+cellSize/2,
                           text=str(board[row][col]),font=("Helvatica", 20, "bold"))

# positive number is snake, 0 is nothing,
# -1 is the food, -2 is a posion, -3 is a wall

def drawSnakeCell2(canvas,board,margin,cellSize,row,col,left,right,top,bottom):
    (snakeColor,foodColor,posionColor) = (canvas.data.colorTuple)
    if (board[row][col] > 0):
        # draw part of the snake body
        canvas.create_oval(left, top, right, bottom, fill= snakeColor)
    elif (board[row][col] == -1):
        # draw food
        canvas.create_oval(left, top, right, bottom, fill= foodColor)
    elif (board[row][col] == -2):
        #draw Posion
        canvas.create_oval(left,top,right,bottom,fill = posionColor)
    elif (board[row][col] == -3):
        #draw Walls
        canvas.create_rectangle(left,top,right,bottom, fill = "brown",width = 4)
    score = canvas.data.score
    # draw a snake head, easier to play
    if board[row][col] == score +1-canvas.data.wallsRuninto-canvas.data.bonus\
       and canvas.data.inDebugMode == False:
            canvas.create_text((left+right)/2,(top+bottom)/2,text = "Head",\
                               fill = "white")

def findHighScores(canvas):
    findScore(canvas)
    # create a high score list, and never delete until program is quit
    if canvas.data.score != 0:
        canvas.data.highScoreList += [canvas.data.score]
    i = sorted(canvas.data.highScoreList)
    r = i[::-1]
    # return a score list from high score to low score
    return r

def findScore(canvas):
    board = canvas.data.board
    rows,cols =  len(board),len(board[0])
    canvas.data.wallsExist = 0
    score = 0
    for row in  xrange(rows):
        for col in xrange(cols):
            if board[row][col] > 0:
                score +=1
            if board[row][col]== -3:
                canvas.data.wallsExist += 1
    if canvas.data.wallsExist > 0 and canvas.data.moves>=20 and score >= 3:
        canvas.data.bonus = 1
    canvas.data.score = score -1-canvas.data.wallsRuninto+canvas.data.bonus
        
def moveSnake(canvas,drow, dcol):
    canvas.data.snakeDrow,canvas.data.snakeDcol = drow,dcol 
    board = canvas.data.board
    rows, cols = len(board),len(board[0])
    headRow,headCol = canvas.data.headRow,canvas.data.headCol
    newHeadRow = headRow + drow
    newHeadCol = headCol + dcol
    # function too long, need a helper function
    moveSnake2(canvas,board,newHeadRow,newHeadCol,headRow,headCol)

def moveSnake2(canvas,board,newHeadRow,newHeadCol,headRow,headCol):
    rows, cols = len(board),len(board[0])
    if ((newHeadRow < 0) or (newHeadRow >= rows) or (newHeadCol < 0) or \
        (newHeadCol >= cols)) or (board[newHeadRow][newHeadCol] > 0) or \
         (board[newHeadRow][newHeadCol] == -2):
        canvas.data.isGameOver = True
    elif (board[newHeadRow][newHeadCol] == -1):
        board[newHeadRow][newHeadCol] = 1 + board[headRow][headCol]
        canvas.data.headRow,canvas.data.headCol = newHeadRow, newHeadCol
        placeFoodAndPosion(canvas)
    elif board[newHeadRow][newHeadCol] == -3:
        canvas.data.wallsRuninto += 1
        board[newHeadRow][newHeadCol] = 1 + board[headRow][headCol]
        canvas.data.headRow,canvas.data.headCol = newHeadRow, newHeadCol
        removeTail(canvas)
    else:
        board[newHeadRow][newHeadCol] = 1 + board[headRow][headCol]
        canvas.data.headRow,canvas.data.headCol = newHeadRow, newHeadCol
        removeTail(canvas)

def removeTail(canvas):
    board = canvas.data.board
    rows, cols = len(board),len(board[0])
    for row in xrange(rows):
        for col in xrange(cols):
            if board[row][col] > 0:
                board[row][col] -= 1

def findSnakeHead(canvas):
    board = canvas.data.board
    rows,cols = len(board),len(board[0])
    headRow,headCol = 0,0
    for row in xrange(rows):
        for col in xrange(cols):
            if board[row][col] > board[headRow][headCol]:
                headRow = row
                headCol = col
    canvas.data.headRow = headRow
    canvas.data.headCol = headCol

def placeFoodAndPosion(canvas):
    board = canvas.data.board
    noFood = [canvas.data.headRow,canvas.data.headCol]
    while True:
        row,col = random.randint(0,canvas.data.rows -1),random.randint(0,\
                                                                       canvas.data.cols-1)
        if [row,col] != noFood and board[row][col] == 0 :
            break
    board[row][col] = -1
    # place posion in level 2
    if  canvas.data.score >= 3 and noHeadAround(canvas) :
        while True:
            row,col = random.randint(0,canvas.data.rows -1),random.randint(\
                0,canvas.data.cols-1)
            if [row,col] != noFood and board[row][col] == 0:
                break
        board[row][col] = -2
        
def noHeadAround(canvas):
    board = canvas.data.board
    r = canvas.data.headRow
    c = canvas.data.headCol
    for i in (-1,2): #check -1,0,1
        for j in (-1,2):
            if r+i < len(board) and r+i > 0 and c+j < len(board[0]) and c+j>0:
                if board[r+i][c+j] > 0:
                    return False
    return True

def mousePressed(canvas,event):
    if canvas.data.isPause == True:
        board = canvas.data.board
        (x,y) = (event.x,event.y)
        if 50<x<canvas.data.Width-50 and 50<y<canvas.data.Height:
            (col,row) = ((x-50)/30, (y-50)/30)
            if board[row][col] == 0:
                board[row][col] = -3
            elif board[row][col] == -3:
                board[row][col] = 0
            redrawAll(canvas)
    

def keyPressed(canvas,event):
    canvas.data.ignoreNextTimerEvent = True
    if event.char == "r":
            init(canvas)
    if canvas.data.isGameOver == False:
        if event.char == "q":
            canvas.data.isGameOver = True
            redrawAll(canvas)
        elif (event.char == "d"):
            canvas.data.inDebugMode = not canvas.data.inDebugMode
        elif (event.keysym == "p"):
            canvas.data.isPause = not canvas.data.isPause
            canvas.data.colorTuple = ("DarkBlue","DarkGreen","DarkRed")
            redrawAll(canvas)
    if canvas.data.isPause == False:
        # function too long, helper function
        keyPressed2(canvas,event)

def keyPressed2(canvas,event):
    if (event.keysym == "Up"):
        moveSnake(canvas,-1, 0)
    elif (event.keysym == "Down"):
        moveSnake(canvas,+1, 0)
    elif (event.keysym == "Left"):
        moveSnake(canvas,0,-1)
    elif (event.keysym == "Right"):
        moveSnake(canvas,0,+1)
        redrawAll(canvas)

    
def run(rows,cols):
    root = Tk()
    margin,cellSize = 50,30
    canvasWidth = 2*margin +cellSize *rows
    canvasHeight = 2*margin + cellSize * cols
    canvas = Canvas(root,width=canvasWidth,height = canvasHeight)
    canvas.pack()
    root.canvas = canvas.canvas = canvas
    class Struct: pass
    canvas.data = Struct()
    canvas.data.Width,canvas.data.Height = canvasWidth,canvasHeight 
    canvas.data.rows,canvas.data.cols = rows,cols
    canvas.data.highScoreList = []
    canvas.data.score, canvas.data.wallsRuninto = 0,0
    canvas.data.moves,canvas.data.wallsExist = 0,0
    init(canvas)
    root.bind("<Key>",lambda event : keyPressed(canvas,event))
    root.bind("<Button-1>",lambda event: mousePressed(canvas,event))
    timerFired(canvas)
    root.mainloop()

run(20,20)
    
