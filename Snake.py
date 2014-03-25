from Tkinter import *
import random

def init(canvas):
    printInstructions()
    loadSnakeBoard(canvas)
    canvas.data.inDebugMode = False
    canvas.data.isGameOver = False
    canvas.data.score = 0
    canvas.data.snakeDrow = -1
    canvas.data.snakeDcol = 0
    canvas.data.ignoreNextTimerEvent = False
    redrawAll(canvas)

def printInstructions():
    print "This is snake made by benson!"
    print "Same as always, eat food to grow, try not to hit the wall or yourself"
    print "Press 'd' for debug mode"
    print "Press 'r' to restart"

def redrawAll(canvas):
    canvas.delete(ALL)
    drawSnakeBoard(canvas)
    if canvas.data.isGameOver == True:
        x = canvas.data.Width/2
        y = canvas.data.Height/2
        findScore(canvas)
        text = "Game over! Your final score is %d" % canvas.data.score
        canvas.create_text(x,y,text=text,font=("Helvetica",30,"bold"))

def findScore(canvas):
    board = canvas.data.board
    rows,cols =  len(board),len(board[0])
    score = 0
    for row in  xrange(rows):
        for col in xrange(cols):
            if board[row][col] > 0:
                score +=1
    canvas.data.score = score -1
    
def drawSnakeBoard(canvas):
    snakeBoard = canvas.data.board
    rows, cols = len(snakeBoard),len(snakeBoard[0])
    for row in range(rows):
        for col in range(cols):
            drawSnakeCell(canvas,snakeBoard, row, col)

def drawSnakeCell(canvas,board, row, col):
    margin = 10
    cellSize = 30
    left = margin + col * cellSize
    right = left + cellSize
    top = margin + row * cellSize
    bottom = top + cellSize
    canvas.create_rectangle(left, top, right, bottom,width = 3, fill="white")
    if (board[row][col] > 0):
        # draw part of the snake body
        canvas.create_oval(left, top, right, bottom, fill="blue")
    elif (board[row][col] < 0):
        # draw food
        canvas.create_oval(left, top, right, bottom, fill="green")
    # for debugging, draw the number in the cell
    if (canvas.data.inDebugMode == True):
        canvas.create_text(left+cellSize/2,top+cellSize/2,
                           text=str(board[row][col]),font=("Helvatica", 20, "bold"))

def moveSnake(canvas,drow, dcol):
    canvas.data.snakeDrow = drow 
    canvas.data.snakeDcol = dcol
    board = canvas.data.board
    rows, cols = len(board),len(board[0])
    headRow = canvas.data.headRow
    headCol = canvas.data.headCol
    newHeadRow = headRow + drow
    newHeadCol = headCol + dcol
    if ((newHeadRow < 0) or (newHeadRow >= rows) or
        (newHeadCol < 0) or (newHeadCol >= cols)):
        canvas.data.isGameOver = True
    elif (board[newHeadRow][newHeadCol] > 0):
        canvas.data.isGameOver = True
    elif (board[newHeadRow][newHeadCol] < 0):
        board[newHeadRow][newHeadCol] = 1 + board[headRow][headCol];
        canvas.data.headRow = newHeadRow
        canvas.data.headCol = newHeadCol
        placeFood(canvas)
    else:
        board[newHeadRow][newHeadCol] = 1 + board[headRow][headCol];
        canvas.data.headRow = newHeadRow
        canvas.data.headCol = newHeadCol
        removeTail(canvas)

def removeTail(canvas):
    board = canvas.data.board
    rows, cols = len(board),len(board[0])
    for row in xrange(rows):
        for col in xrange(cols):
            if board[row][col] > 0:
                board[row][col] -= 1

def loadSnakeBoard(canvas):
    rows, cols = canvas.data.rows, canvas.data.cols
    snakeBoard = []
    for row in xrange(rows):
        rowList = []
        for col in xrange(cols):
            rowList += [0]
        snakeBoard += [rowList]
    snakeBoard[rows/2][cols/2] = 1
    canvas.data.board = snakeBoard
    findSnakeHead(canvas)
    placeFood(canvas)

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

def placeFood(canvas):
    board = canvas.data.board
    noFood = [canvas.data.headRow,canvas.data.headCol]
    row,col = random.randint(0,canvas.data.rows -1),random.randint(0,canvas.\
                                                                 data.cols-1)
    if [row,col] != noFood:
        board[row][col] = -1
        
    
def timerFired(canvas):
    ignoreThisTimerEvent = canvas.data.ignoreNextTimerEvent
    canvas.data.ignoreNextTimerEvent = False
    if canvas.data.isGameOver == False and ignoreThisTimerEvent == False :
        drow = canvas.data.snakeDrow
        dcol = canvas.data.snakeDcol
        moveSnake(canvas,drow,dcol)
        redrawAll(canvas)
    delay = 200
    canvas.after(delay,timerFired,canvas)

def keyPressed(canvas,event):
    canvas.data.ignoreNextTimerEvent = True
    if event.char == "q":
        canvas.data.isGameOver = True
    elif event.char == "r":
        init(canvas)
    elif (event.char == "d"):
        canvas.data.inDebugMode = not canvas.data.inDebugMode
    if (canvas.data.isGameOver == False):
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
    margin = 10
    cellSize = 30
    canvasWidth = 2*margin +cellSize *rows
    canvasHeight = 2*margin + cellSize * cols
    canvas = Canvas(root,width=canvasWidth,height = canvasHeight)
    canvas.pack()
    root.canvas = canvas.canvas = canvas
    class Struct: pass
    canvas.data = Struct()
    canvas.data.Width = canvasWidth
    canvas.data.Height = canvasHeight
    canvas.data.rows = rows
    canvas.data.cols = cols
    init(canvas)
    root.bind("<Key>",lambda event : keyPressed(canvas,event))
    timerFired(canvas)
    root.mainloop()

run(20,20)
    
