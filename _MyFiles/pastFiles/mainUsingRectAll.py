# Owener: Benson zhang
# Created date: Apr. 1st
# Images from http://piq.codeus.net/draw?base_piq=16492
#               and from http://opengameart.org/


import pygame
from pygame.locals import *
import random
import math
import sys

pygame.init()
screen = pygame.display.set_mode((800,600))
pygame.display.set_caption("Trapped")
background = pygame.image.load("image/background.png").convert()
###################################
###################################
########### VECTORS  ##############
###################################
###################################

# Used for rifle
class vector(object):     
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def __sub__(self, other): # subtract
            return vector(self.x - other.x, self.y - other.y)
        
    def __repr__(self): # print
        return "(%s, %s)"%(self.x, self.y)

    def __getitem__(self, key): # get one of the values from the vector
        return (self.x, self.y)[key]

    def get_length(self): # gets the length of a vector
        return math.sqrt((self.x**2 + self.y**2))

    def normalize(self): # this is dividing something by its length used for direction to move
        length = self.get_length() # gets the length
        if length != 0: # if we are not going to divide by zero
            return(self.x / length, self.y / length) # divides (x, y) by the length
        return (self.x, self.y) # if length == 0 then skip the division step

###################################
###################################
########### MONSTER ##############
###################################
###################################

class monster(pygame.sprite.Sprite):
    def __init__(self, xPos = 500):
        pygame.sprite.Sprite.__init__(self)
        #super(monster,self).__init__()
        self.image = pygame.image.load("image/chump_standLeft.png")
        self.rect = self.image.get_rect()
        self.rect.centerx, self.centery = xPos,550
        self.imageRight = load_sliced_sprites(28, 24, 'chump_runRight.png')
        self.imageLeft = load_sliced_sprites(28, 24, 'chump_runLeft.png')   
        self.dirFace = True # true is right, false is left
        self.attacking = False
        self.offGround = False
        self.xSpeed = 0
        self.ySpeed = 0
        self.isRunning = False
        self.gravity = 1
        
        self.monsterHit = 0
        self.die = False

        self._delay = 100
        self._last_update = 0
        self._frame = 0

    def getMonsterCondition(self):
        return [self.rect.centerx, self.rect.centery, self.die]

    def AI(self):
        if self.rect.centery >= 400:
            if 0 > self.rect.centerx - self.playerPos[0] > -300:
                self.dirFace = True
                self.isRunning = True
            elif 0 <= self.rect.centerx - self.playerPos[0] < 300:
                self.dirFace = False
                self.isRunning = True
            else:
                if self.dirFace and self.rect.centerx < 700:
                    self.isRunning = True
                elif not self.dirFace and self.rect.centerx > 0:
                    self.isRunning = True
                else:
                    self.dirFace = not self.dirFace
                    self.isRunning = False
                    self.playerIsJumping = False
                    
            if self.playerIsJumping:
                if not self.offGround:
                    self.offGround = True
                    self.ySpeed = -10
        
    def update(self, playerCond):
        self.playerIsJumping = playerCond[2]
        self.playerPos = playerCond[:2]
        
        t = pygame.time.get_ticks()
        self.rightFace = load_sliced_sprites(28, 24, "chump_runRight.png")
        self.leftFace = load_sliced_sprites(28, 24, "chump_runLeft.png")
        
        if self.monsterHit < 2:
            self.AI()
            
            if not self.isRunning and not self.offGround:
                self.xSpeed = 0
                self.ySpeed = 0
                if self.dirFace:
                    self.image = pygame.image.load("image/chump_standRight.png")
                else:
                    self.image = pygame.image.load("image/chump_standLeft.png")
                
            if self.isRunning:
                if self.dirFace:
                    if self.xSpeed <= 2:
                        self.xSpeed += 1
                    else:
                        self.xSpeed = 2
                    # From <Making Games with Python&Pygame> by Al Sweigart
                    if t - self._last_update > self._delay:
                            self._frame += 1
                            if self._frame >= len(self.rightFace):
                                self._frame = 0
                            self.image = self.rightFace[self._frame]
                            self._last_update = t
                elif not self.dirFace:
                    if self.xSpeed >= -2:
                        self.xSpeed -=1
                    else:
                        self.xSpeed = -2
                    if t - self._last_update > self._delay:
                        self._frame += 1
                        if self._frame >= len(self.leftFace): 
                            self._frame = 0
                        self.image = self.leftFace[self._frame]
                        self._last_update = t
                        
            if self.offGround:
                if self.ySpeed < 0:
                    self.ySpeed += self.gravity
                elif self.ySpeed >= 0:
                    self.ySpeed = 15
                if self.dirFace:
                    self.image = pygame.image.load("image/chump_jumpRight.png")
                else:
                    self.image = pygame.image.load("image/chump_jumpLeft.png")

            self.rect.centerx += self.xSpeed
            self.rect.centery += self.ySpeed
            if self.rect.centerx >= 800 - self.rect.width:
                self.rect.centerx = 800- self.rect.width
                self.isRunning = False
            elif self.rect.centerx <= 0:
                self.rect.centerx = 0
                self.isRunning = False
            if self.rect.centery >= 550:
                self.rect.centery = 550
                self.offGround = False
            elif self.rect.centery <= 300:
                self.offGround = True
                
        if self.monsterHit >= 2:
            self.die = True
            
        if self.die:
            self.rect.centery = 550
            self.image = pygame.image.load("image/tombstone.png")
            self.image = pygame.transform.scale(self.image,(24,24))

        screen.blit(self.image,(self.rect.centerx,self.rect.centery))


class Unused(pygame.sprite.Sprite):
    def __init__(self,nothing):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load("image/chump_standLeft.png")
        self.rect = self.image.get_rect()  
        self.monsterHit = 0
        self.alive = True
        
    def update(self,nothing):
        pass
        
    def reset(self, x, y):
        pass

    def getMonsterCondition(self):
        return (-1000, -1000,False)


###################################
###################################
########### PLAYER   ###############
###################################
###################################
        
class player(pygame.sprite.Sprite):
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load("image/bit_standRight.png")
        self.rect = self.image.get_rect()
        self.dirFace = True # true is right, false is left
        self.attacking = False
        self.offGround = False
        self.xSpeed = 0
        self.ySpeed = 0
        self.isRunning = False
        self.gravity = 1
        self.rect.centerx, self.rect.centery = 0,550
        self.coolDown = 10
        self.health = 1000
        
        self.weaponPos = (self.rect.centerx+20,self.rect.centery-20)
        self.weaponName = None
        self.axeSwung = False

        self.spikyBallList = pygame.sprite.Group()
        self.spikyBallActivated = False
        self.spikyBallDropped = False
        self.spikyBallPos = (0,550)
        self.spikyTouch = False

        self.aimPos = (0,0)
        self.rifleFired = False
        self.bulletActivated = False
        self.notHit = True
        self.bulletList = pygame.sprite.Group()
        
        global gameOver
        self._delay = 100
        self._last_update = 0
        self._frame = 0

    def getPlayerCondition(self):
        return (self.rect.centerx,self.rect.centery,self.offGround)

    def getAttackingCondition(self):
        if self.weaponName == "axe":
            return [self.weaponPos[0], self.weaponPos[1] , self.axeSwung]
        elif self.weaponName == "spiky balls":
            return [self.spikyBallPos[0], self.spikyBallPos[1], self.spikyBallDropped]
        elif self.weaponName == "rifle":
            return [1000,1000,self.bulletActivated]
        else:
            return [1000,1000,False]
        
    def keyRead(self):
        keys = pygame.key.get_pressed()
        if keys[K_UP] and not self.attacking:
            if not self.offGround:
                self.offGround = True
                self.ySpeed = -15
        if keys[pygame.K_RIGHT] and not self.attacking:
            self.dirFace = True
            self.isRunning = True
        elif keys[pygame.K_LEFT] and not self.attacking:
            self.dirFace = False
            self.isRunning = True
        else:
            self.isRunning = False
        if keys[K_x] and not self.attacking:
            if self.weaponName == "axe":
                if self.coolDown <= 0:
                    self.coolDown = 10
                    self.attacking = True
                else:
                    self.coolDown -= 1
            elif self.weaponName == "spiky balls":
                self.spikyBallDropped = True
                self.spikyBallPos = (self.rect.centerx, self.rect.centery)
                
    def rifleKeyRead(self):
        if pygame.mouse.get_pressed()[0]:
            self.rifleFired = True
            self.bulletActivated = True
        else:
            self.rifleFired = False
        self.aimPos = pygame.mouse.get_pos()
        
    def update(self, monsterTouch, itemSelected, healthRegenCoolDown,spikyTouch):
        t = pygame.time.get_ticks()
        screen.blit(background,(0,0))
        
        self.rightFace = load_sliced_sprites(28, 24, "bit_runRight.png")
        self.leftFace = load_sliced_sprites(28, 24, "bit_runLeft.png")
        
        self.axeSwung = False
        self.keyRead()
        
        if not self.isRunning and not self.offGround:
            self.xSpeed = 0
            self.ySpeed = 0
            if self.dirFace:
                self.image = pygame.image.load("image/bit_standRight.png")
            else:
                self.image = pygame.image.load("image/bit_standLeft.png")
                
        if self.isRunning:
            self.isRunning = False
            if self.dirFace:
                if self.xSpeed <= 8:
                    self.xSpeed += 1
                else:
                    self.xSpeed = 8
                # From <Making Games with Python&Pygame> by Al Sweigart
                if t - self._last_update > self._delay:
                        self._frame += 1
                        if self._frame >= len(self.rightFace):
                            self._frame = 0
                        self.image = self.rightFace[self._frame]
                        self._last_update = t
            elif not self.dirFace:
                if self.xSpeed >= -8:
                    self.xSpeed -=1
                else:
                    self.xSpeed = -8
                if t - self._last_update > self._delay:
                    self._frame += 1
                    if self._frame >= len(self.leftFace): 
                        self._frame = 0
                    self.image = self.leftFace[self._frame]
                    self._last_update = t
                    
        if self.offGround:
            if self.ySpeed < 0:
                self.ySpeed += self.gravity
            elif self.ySpeed >= 0:
                self.ySpeed = 15
            if self.dirFace:
                self.image = pygame.image.load("image/bit_jumpRight.png")
            else:
                self.image = pygame.image.load("image/bit_jumpLeft.png")

        self.rect.centerx += self.xSpeed
        self.rect.centery += self.ySpeed
        
        if self.rect.centerx >= 800 - self.rect.width:
            self.rect.centerx = 800- self.rect.width
            self.isRunning = False
        elif self.rect.centerx <= self.rect.width:
            self.rect.centerx = self.rect.width
            self.isRunning = False
        if self.rect.centery >= 550:
            self.rect.centery = 550
            self.offGround = False
        elif self.rect.centery <= 300:
            self.offGround = True
            
        screen.blit(self.image, (self.rect.centerx,self.rect.centery))
        
        if monsterTouch and self.health > 0:
            self.health -= 5
        elif not monsterTouch and healthRegenCoolDown <= 0:
            if self.health <= 1000:
                self.health += 0.5
            elif self.health > 1000:
                self.health = 1000
        elif self.health <= 0:
            global gameOver
            gameOver = True
            
        self.healthBar()
            
        if itemSelected != None:
            self.weaponName = itemSelected
            if itemSelected == "axe":
                weapon = pygame.image.load("image/axe.png")
                weapon = pygame.transform.scale(weapon,(32,32))
                self.weaponRect = weapon.get_rect()
                if self.dirFace:
                    self.weaponPos = (self.rect.centerx+20,self.rect.centery-20)
                    angle = -70
                if not self.dirFace:
                    weapon = pygame.transform.flip(weapon,True,False)
                    self.weaponPos = (self.rect.centerx-30,self.rect.centery-20)
                    angle = 70
                if self.attacking:
                    self.attacking = False
                    self.axeSwung = True
                    weapon, weaponRect = rot_center(weapon,self.weaponRect, angle)
                
            elif itemSelected == "spiky balls" and self.spikyBallDropped:
                self.spikyTouch = spikyTouch
                self.createSpikyBall()
                self.spikyBallDropped = False

            elif itemSelected == "rifle":
                self.rifleKeyRead()
                weapon = pygame.image.load("image/rifle.png")
                weapon = pygame.transform.scale(weapon,(36,22))
                if self.dirFace:
                    self.weaponPos = (self.rect.centerx+5, self.rect.centery+3)
                elif not self.dirFace:
                    self.weaponPos = (self.rect.centerx-10, self.rect.centery+3)
                    weapon = pygame.transform.flip(weapon,True,False)
                if self.rifleFired:
                    self.bulletShot()
            screen.blit(weapon, self.weaponPos)
                
        if self.spikyBallActivated:
            self.spikyBallSprites.update(self.spikyTouch)
            
        if self.bulletActivated:
            for i in self.bulletList:
                curPos = i.getBulletCondition()
                if self.notHit:
                    if curPos[0] <= 0 or curPos[0] >= 800 or curPos[1] <= 0 or curPos[1] >= 600:
                        self.bulletList.remove(i)
            if len(self.bulletList) != 0:
                self.bulletSprites.update()
                        
    def healthBar(self):
        font = pygame.font.Font(None, 24)
        text = font.render("HP: ",1,(0,0,0))
        pygame.draw.rect(screen,(0,0,0),(50,50,101,31),1)
        pygame.draw.rect(screen,(255,0,0),(51,51,(self.health-1)/10,29),0)
        screen.blit(text,(20,60))
        
    def createSpikyBall(self):
        spiky = spikyBalls(self.spikyBallPos)
        remove = 0
        self.spikyBallList.add(spiky)
        self.spikyBallActivated = True
        self.spikyBallSprites = pygame.sprite.OrderedUpdates(self.spikyBallList)
        for s in self.spikyBallList:
            if self.spikyTouch:
                self.spikyBallList.remove(s)
        
    def getSpikyBallList(self):
        return self.spikyBallList

    def bulletShot(self):
        if self.dirFace:
            offset = 20
        else:
            offset = -30
        b = bullets((self.rect.centerx+offset,self.rect.centery), self.aimPos)
        self.bulletList.add(b)
        self.bulletSprites = pygame.sprite.OrderedUpdates(self.bulletList)
        
        
# from http://www.pygame.org/wiki/RotateCenter
def rot_center(image, rect, angle):
        """rotate an image while keeping its center"""
        rot_image = pygame.transform.rotate(image, angle)
        rot_rect = rot_image.get_rect(center=rect.center)
        return rot_image,rot_rect

def load_sliced_sprites(w, h, filename):
    images = []
    filename = "image/"+filename
    master_image = pygame.image.load(filename).convert_alpha()
    master_width, master_height = master_image.get_size()
    for i in range(int(master_width / w)):
        images.append(master_image.subsurface((i * w, 0, w, h)))
    return images

###################################
###################################
########### SPIKY BALLS  ###########
###################################
###################################
class spikyBalls(pygame.sprite.Sprite):
    def __init__(self,dropPos):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load("image/spikyballs.png")
        self.image = pygame.transform.scale(self.image, (12,12))
        self.rect = self.image.get_rect()
        self.dropTime = pygame.time.get_ticks()
        self.deleteTime = 500
        
        self.rect.centerx, self.rect.centery = dropPos
        self.touched = False
        self.offGround = False
        self.gravity = 1
        self.ySpeed = 0
        
    def __hash__(self):
        hashables = (self.rect.centerx,self.rect.centery)
        result = 0
        for value in hashables:
            result = 33*result + hash(value)
        return hash(result)
    
    def update(self, touched):
        self.touched = touched
        
        if self.rect.centery <= 560:
            self.ySpeed += self.gravity
        if self.rect.centery > 560:
            self.ySpeed = 0
            self.rect.centery = 560
        self.rect.centery += self.ySpeed
        
        if self.deleteTime >= 0:
            self.deleteTime -= 1
        else:
            self.touched = True
            
        if not self.touched:
            screen.blit(self.image, (self.rect.centerx, self.rect.centery))

    def getSpikyBallCondition(self):
        if not self.touched:
            return (self.rect.centerx, self.rect.centery)
        else:
            return None

###################################
###################################
########### BULLETS   ##############
###################################
###################################
class bullets(pygame.sprite.Sprite):
    def __init__(self,shootingPos,aimPos):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load("image/bullet.png")
        self.image = pygame.transform.scale(self.image,(10,10))
        self.rect = self.image.get_rect()
        self.rect.centerx, self.rect.centery = float(shootingPos[0]), float(shootingPos[1])
 #       print "should be the same as bullet BLIT point", (self.rect.centerx, self.rect.centery)
        self.originalY = self.rect.centery
        self.targetx, self.targety = float(aimPos[0]), float(aimPos[1])
        self.speed = 10.0
        self.start = vector(self.rect.centerx, self.rect.centery)
        self.end = vector(self.targetx, self.targety)
        self.distance = self.end - self.start
        self.dir = self.distance.normalize()
                                
    def getBulletCondition(self):
        return (self.rect.centerx, self.rect.centery)
    
    def update(self):
        screen.blit(self.image,(self.rect.centerx, self.rect.centery))
        """
        radians = math.atan2(self.start[0],self.end[1])
        dx = math.cos(radians)
        dy = math.sin(radians)
        self.rect.centerx += dx*self.speed
        self.rect.centery += dy*self.speed
        """
        self.rect.centerx += self.dir[0]*self.speed
        self.rect.centery += self.dir[1]*self.speed
        

        

###################################
###################################
########### PAUSE MENU  ###########
###################################
###################################
class pausedMenu(object):
    def __init__(self):
        self.weapons = ["Weapons:","spiky balls","axe","rifle"]
        self.attack= 10
        self.items = ["Items:","NULL","NULL","NULL"]
        self.helthPoint = 200
        self.line = 0
        self.col = 1
        self.needUpdate = True
        self.selected = False

    def keyRead(self):                
        keys = pygame.key.get_pressed()
        pygame.time.delay(100)
        if keys[K_UP]:
            self.needUpdate = True
            self.line = (self.line -1)%2
            self.selected = False
        if keys[K_RIGHT]:
            self.selected = False
            self.needUpdate = True
            self.col = (self.col+1)% 3
        elif keys[K_LEFT]:
            self.selected = False
            self.needUpdate = True
            self.col = (self.col-1)%3
        elif keys[K_DOWN]:
            self.selected = False
            self.needUpdate = True
            self.line = (self.line+1)%2
        elif keys[K_RETURN]:
            self.selected = not self.selected

    def getItemSelected(self):
        if self.selected :
            if self.line == 0:
                return self.weapons[self.col+1]
            else:
                return self.items[self.col+1]
        
    def update(self, needUpdate):
        self.keyRead()
        self.needUpdate = needUpdate
        
        if self.needUpdate:
            self.needUpdate = False
            s = pygame.Surface((800,300))
            #s.set_alpha(0)
            s.fill((164,164,164))
            
            boxPosX = 177 + self.col*150
            boxPosY = 67 + self.line*100
            font = pygame.font.Font(None, 24)

            for j in xrange(len(self.weapons)):
                name = self.weapons[j]
                text = font.render(name,1,(0,0,0))
                textpos = text.get_rect()
                textpos.centerx = screen.get_rect().centerx - 300 + j*150
                textpos.centery = screen.get_rect().centery-200
                s.blit(text,textpos)
    
            for i in xrange(len(self.items)):
                name = self.items[i]
                text = font.render(name,1,(0,0,0))
                textpos = text.get_rect()
                textpos.centerx = screen.get_rect().centerx - 300 + i*150
                textpos.centery = screen.get_rect().centery-100
                s.blit(text,textpos)

            if self.selected:
                color = (255,0,0)
            else:
                color = (0,0,0)
                
            pygame.draw.rect(s,color,(boxPosX,boxPosY, 150,50),1)
            screen.blit(s,(0,0))

            
###################################
###################################
########### FLOOR   ################
###################################
###################################
class FloorLong(pygame.sprite.Sprite):
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load("image/floor_long.png")
        self.image = pygame.transform.scale(self.image,(800,30))
        self.rect = self.image.get_rect()
        self.rect.centery = 560
        self.rect.centerx = 400
        
    def update(self,playerCond):
        screen.blit(self.image, (0,575))


###################################
###################################
########### RUN FUNCTION  #########
###################################
###################################

def run(difficulty):
    global gameOver
    Paused = False
    monsterTouch = False
    itemSelected = "axe"
    skipPlayerUpdate= False
    spikyBallActivated = False
    spikyTouch = False
    healthRegenCoolDown = 500
    
    groundFloor = FloorLong()
    terrain = pygame.sprite.OrderedUpdates(groundFloor)
                                              
    you = player()
    playerSprite = pygame.sprite.OrderedUpdates(you)
    
    menu = pausedMenu()

    if difficulty == 0:
        monster1 = monster()
        monster2 = Unused(600)
        monster3 = Unused(700)
        rebornTime = reset = 200
    elif difficulty ==1:
        monster1 = monster()
        monster2 = monster(600)
        monster3 = Unused(700)
        rebornTime = reset = 100
    elif difficulty == 2:
        monster1 = monster()
        monster2 = monster(600)
        monster3 = monster(700)
        rebornTime = reset = 10
        
    monsterList = pygame.sprite.Group(monster1,monster2,monster3)
    enemySprites = pygame.sprite.OrderedUpdates(monsterList)

    spikyBallList = you.getSpikyBallList()
    
    while not gameOver:
        if Paused:
            menu.update(True)
            itemSelected = menu.getItemSelected()
            
        elif not Paused:
            
            playerCond = you.getPlayerCondition()
            attackingCond = you.getAttackingCondition()

            for m in monsterList:
                monsterCond = m.getMonsterCondition()
            
                # check collision
                if -24 <= playerCond[0] - monsterCond[0] <= 24 and\
                   -24 <= playerCond[1] - monsterCond[1] <= 24 and\
                   not monsterCond[2]:
                    healthRegenCoolDown = 500
                    monsterTouch = True
                    skipPlayerUpdate =True
                    playerSprite.update(monsterTouch, itemSelected, healthRegenCoolDown,spikyTouch)
                else:
                    if not playerCond[2]:
                        healthRegenCoolDown -= 1
                    else:
                        healthRegenCoolDown = 500
                    monsterTouch = False

                #if itemSelected != None:
                
                if attackingCond[2] == True:
                    if itemSelected == "axe":
                        if -32<= attackingCond[0] - monsterCond[0] <= 32 and\
                           -32<= attackingCond[1] - monsterCond[1] <= 32:
                            m.monsterHit += 2
                            
                if itemSelected == "spiky balls":
                    for s in spikyBallList:
                        pos = s.getSpikyBallCondition()
                        if pos != None:
                            x,y = pos[0], pos[1]
                            if -10<= x - monsterCond[0] <= 10 and\
                               -10 <= y - monsterCond[1] <= 10:
                                m.monsterHit += 0.5
                                spikyTouch =True
                            elif y == 560 and \
                                 -10<= x - playerCond[0] <= 10 and\
                                 -10 <= y - playerCond[1] - 16 <= 10:
                                spikyTouch =True
                                monsterTouch = True
                                
                if monsterCond[2] == True:
                    if rebornTime >= 0:
                        rebornTime -= 1
                    else:
                        rebornTime = reset
                        newOne = monster(random.randint(100,700))
                        monsterList.add(newOne)
                        monsterList.remove(m)
                        enemySprites = pygame.sprite.OrderedUpdates(monsterList)
                        
            if skipPlayerUpdate:
                skipPlayerUpdate = False
            else:
                playerSprite.update(monsterTouch, itemSelected, healthRegenCoolDown,spikyTouch)
                
            enemySprites.update(playerCond)
            terrain.update(playerCond)
            
        pygame.display.update()
        
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_p or event.key == pygame.K_ESCAPE:
                    Paused = not Paused
                if event.key == pygame.K_r :
                    startingScreen()

def bensonGameIntro():
    first = "Hi what up guys, this is what I made for 15112 term project"
    second = "This game is really basic, press P or ESC for pause menu"
    secondMore = "In pause menu you may choose your weapons, hit ENTER to choose"
    secondEvenMore = "when you see the red box, you have selected it, and X to attack"
    secondOMGSoMuch="And then you can press P or ESC again to quit pause menu"
    third = "use arrow keys to control you little man in this windows"
    fourth = "The monsters will come at you, and if they die, they regenerate"
    fifth = "HAVE FUN! oh btw, Hit ENTER to continue! R to restart!"
    return [first,second,secondMore,secondEvenMore,secondOMGSoMuch,third,fourth,fifth]

                                
def menuDisplay(difficulty):
    font = pygame.font.Font(None, 48)
    if difficulty == 0:
        text = font.render("EASY",1,(0,255,0))
    elif difficulty == 1:
        text = font.render("Normal",1,(0,0,255))
    elif difficulty == 2:
        text = font.render("HELL",1,(255,0,0))

    textpos = text.get_rect()
    textpos.centerx = screen.get_rect().centerx
    textpos.centery = screen.get_rect().centery+180
    menu = font.render("Select diffculty: ",1,(0,0,0))
    menupos = text.get_rect()
    screen.blit(menu,(300,420))
    screen.blit(text,textpos)

def startingScreen():
    image = pygame.image.load("image/starting.jpg").convert()
    screen.blit(image,(0,0))
    font = pygame.font.Font(None, 48)
    text = font.render("TRAPPED",1,(0,0,0))
    textpos = text.get_rect()
    textpos.centerx = image.get_rect().centerx
    image.blit(text,textpos)
    introinfo = bensonGameIntro()

    global gameOver
    gameOver = False
    
    notStarting = True
    difficulty = 1
    
    while notStarting:
        font = pygame.font.Font(None, 36)
        screen.blit(image, (0, 0))
        
        for i in xrange(len(introinfo)):
            text = introinfo[i]
            text = font.render(text,1,(0,0,255))
            textpos = text.get_rect()
            textpos.centerx = screen.get_rect().centerx
            textpos.centery = 50+(50*i)
            
            screen.blit(text,textpos)
            
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_RETURN:
                    notStarting = False
                if event.key == pygame.K_RIGHT:
                    difficulty = (difficulty + 1)%3
                elif event.key == pygame.K_LEFT:
                    difficulty = (difficulty - 1) % 3
                
        menuDisplay(difficulty)
        pygame.display.update()
    
    if not notStarting:
        run(difficulty)
      
    if gameOver:
        font = pygame.font.Font(None, 64)
        text = font.render("GAME OVER! press R to restart",1,(0,0,0))
        textpos = text.get_rect()
        textpos.centerx = screen.get_rect().centerx
        textpos.centery = screen.get_rect().centery
    
        screen.blit(text,textpos)   
        pygame.display.update()
        while True:
            for event in pygame.event.get():
                if event.type == QUIT:
                    pygame.quit()
     #               sys.exit()
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_r :
                        startingScreen()


startingScreen()
