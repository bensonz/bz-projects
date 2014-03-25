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
    def __sub__(self, other): # subtract between vectors
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
        self.image = pygame.image.load("image/chump_standLeft.png")
        self.rect = self.image.get_rect()
        self.rect.centerx, self.centery = xPos,550 # x position changable
        self.imageRight = load_sliced_sprites(28, 24, 'chump_runRight.png')
        self.imageLeft = load_sliced_sprites(28, 24, 'chump_runLeft.png')   
        self.dirFace = True # true is right, false is left
        self.offGround = False # start on ground
        self.xSpeed, self.ySpeed = 0,0 # initial speed is 0
        self.isRunning = False # start out standing
        self.gravity = 1 # gravity, simulate real world physics
        self.monsterHit = 0  # keep track of hitting monster
        self.die = False # start out alive
        self.once = True  # drop items only once
        self.dropitems = [] # don't drop anything yet
        self._delay = 100  # frame delay, to make it better
        self._last_update = 0
        self._frame = 0

    def getMonsterCondition(self):
        """ return monste positions and if it died and if drop tiems"""
        return [self.rect.centerx, self.rect.centery, self.die,self.dropitems]

    def AI(self):
        """simulate artificial moves"""
        if self.rect.centery >= 400:
            if 0 > self.rect.centerx - self.playerPos[0] > -300:
                self.dirFace,self.isRunning = True, True
            elif 0 <= self.rect.centerx - self.playerPos[0] < 300:
                self.dirFace,self.isRunning = False,True
            else:
                if self.dirFace and self.rect.centerx < 700:
                    self.isRunning = True
                elif not self.dirFace and self.rect.centerx > 0:
                    self.isRunning = True
                else:
                    self.dirFace = not self.dirFace
                    self.isRunning,self.playerIsJumping = False,False
            if self.playerIsJumping and not self.offGround:
                    self.offGround = True
                    self.ySpeed = -10

    def update(self, playerCond):
        self.playerIsJumping = playerCond[2]
        self.playerPos = playerCond[:2]
        t = pygame.time.get_ticks()
        self.rightFace = load_sliced_sprites(28, 24, "chump_runRight.png")
        self.leftFace = load_sliced_sprites(28, 24, "chump_runLeft.png")
        if self.monsterHit < 20:
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
                
        if self.monsterHit >= 20:
            self.die = True
            
        if self.die:
            if self.once:
                self.dropitems = [self.rect.centerx, self.rect.centery, True]
                self.once =False
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
        self.die = False
        
    def update(self,nothing):
        pass
        
    def reset(self, x, y):
        pass

    def getMonsterCondition(self):
        return (-1000, -1000,False,False)


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
        self.bulletList = pygame.sprite.Group()
        
        self.invincibleActivated = False
        
        if not pygame.mixer:
            print "problem with sound"
        else:
            pygame.mixer.init()
            self.snd_playerHurt = pygame.mixer.Sound("Sounds/Player/eight_bits_blip.ogg")
            self.snd_rifleAttack = pygame.mixer.Sound("Sounds/Player/gun.ogg")
            self.snd_jump = pygame.mixer.Sound("Sounds/Player/jump.ogg")
        
        global gameOver
        self._delay = 100
        self._last_update = 0
        self._frame = 0
        
    def keyRead(self):
        keys = pygame.key.get_pressed()
        if keys[K_UP] and not self.attacking:
            if not self.offGround:
                self.snd_jump.play()
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
 #                   self.snd_axeAttack.play()
                else:
                    self.coolDown -= 1
            elif self.weaponName == "spiky balls":
                self.spikyBallDropped = True
                self.spikyBallPos = (self.rect.centerx, self.rect.centery)
            else:
                self.itemUsed = True
                
    def rifleKeyRead(self):
        if pygame.mouse.get_pressed()[0]:
            self.rifleFired = True
            self.bulletActivated = True
            self.snd_rifleAttack.play()
        else:
            self.rifleFired = False
        self.aimPos = pygame.mouse.get_pos()
        
    def update(self, monsterTouch, itemSelected, healthRegenCoolDown):
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
        # update player sprite
        screen.blit(self.image, (self.rect.centerx,self.rect.centery))
        
        if itemSelected == "invincible":
            monsterTouch = False
            
        if monsterTouch and self.health > 0:
            self.snd_playerHurt.play()
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
            
        if itemSelected in ["axe","spiky balls","rifle"]:
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
                
            elif itemSelected == "spiky balls":
                weapon = pygame.image.load("image/spikyBalls.png")
                weapon = pygame.transform.scale(weapon,(10,10))
                if self.dirFace:
                    self.weaponPos = (self.rect.centerx +20, self.rect.centery+5)
                else:
                    self.weaponPos = (self.rect.centerx - 9, self.rect.centery+5)
                if self.spikyBallDropped:
                    self.createSpikyBall()
                    self.spikyBallActivated = True
                    self.spikyBallDropped = False
                    
            elif itemSelected == "rifle":
                self.rifleKeyRead()
                self.aimPointUpdate()
                weapon = pygame.image.load("image/rifle.png")
                weapon = pygame.transform.scale(weapon,(36,22))
                if self.dirFace:
                    self.weaponPos = (self.rect.centerx+6, self.rect.centery+5)
                elif not self.dirFace:
                    self.weaponPos = (self.rect.centerx-15, self.rect.centery+5)
                    weapon = pygame.transform.flip(weapon,True,False)
                if self.rifleFired:
                    self.bulletShot()
            screen.blit(weapon, self.weaponPos)
            
        elif itemSelected in ["invincible","anti-gravity","bricks"]:
            if itemSelected == "invincible":
                self.image = pygame.transform.scale(self.image, (90,90))
                self.rect = self.image.get_rect()
                
        if self.spikyBallActivated:
            for s in self.spikyBallList:
                curPos = s.getSpikyBallCondition()
                if curPos[2]:
                    self.spikyBallList.remove(s)
            if len(self.spikyBallList) != 0:
                self.spikyBallList.update()
            else:
                self.spikyBallActivated = False
            
        if self.bulletActivated:
            for i in self.bulletList:
                curPos = i.getBulletCondition()
                if not curPos[2]:
                    if curPos[0] <= 0 or curPos[0] >= 800 or curPos[1] <= 0 or curPos[1] >= 600:
                        self.bulletList.remove(i)
                elif curPos[2]:
                    self.bulletList.remove(i)
            if len(self.bulletList) != 0:
                self.bulletSprites.update()
            else:
                self.bulletActivated = False
                
    def aimPointUpdate(self):
        image = pygame.image.load("image/aimPoint.png")
        if self.rifleFired:
            image = pygame.transform.scale(image,(30,30))
        else:
            image = pygame.transform.scale(image,(20,20))
        screen.blit(image,self.aimPos)
                
    def getPlayerCondition(self):
        return (self.rect.centerx,self.rect.centery,self.offGround)

    def getAttackingCondition(self):
        if self.weaponName == "axe":
            return [self.weaponPos[0], self.weaponPos[1] , self.axeSwung]
        else:
            return [1000,1000,False]
                        
    def healthBar(self):
        font = pygame.font.Font(None, 24)
        text = font.render("HP: ",1,(0,0,0))
        pygame.draw.rect(screen,(0,0,0),(50,50,101,31),1)
        pygame.draw.rect(screen,(255,0,0),(51,51,(self.health-1)/10,29),0)
        screen.blit(text,(20,60))
        
    def createSpikyBall(self):
        spiky = spikyBalls(self.spikyBallPos, self.dirFace)
        self.spikyBallList.add(spiky)
        self.spikyBallSprites = pygame.sprite.OrderedUpdates(self.spikyBallList)

    def bulletShot(self):
        if self.dirFace:
            offset = 20
        else:
            offset = -30
        b = bullets((self.rect.centerx+offset,self.rect.centery), self.aimPos)
        self.bulletList.add(b)
        self.bulletSprites = pygame.sprite.OrderedUpdates(self.bulletList)

    def getSpikyBallList(self):
        return self.spikyBallList

    def getBulletList(self):
        return self.bulletList
        
        
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
########### ITEMS##################
###################################
###################################
class items(pygame.sprite.Sprite):
    def __init__(self,xPos,yPos):
        pygame.sprite.Sprite.__init__(self)
        r = random.randint(1,3)
        if r == 1:
            self.name = "image/invincible.png"
        elif r== 2:
            self.name = "image/brick.png"
        else:
            self.name = "image/gravity.png"
        self.image = pygame.image.load(self.name)
        self.image = pygame.transform.scale(self.image,(15,15))
        self.rect = self.image.get_rect()
        self.rect.centerx,self.rect.centery = xPos, yPos
        self.pickedUp = False
        self.dir = random.choice((True, False))
        if self.dir:
            self.xSpeed = 10
        else:
            self.xSpeed = -10
        self.gravity = 1
        self.ySpeed = -7
        self.deleteTime = 500
        self.skipUpdate = False
        
    def update(self):
        screen.blit(self.image, (self.rect.centerx,self.rect.centery))
        if not self.skipUpdate:
            self.skipUpdate = True
            self.rect.centerx += self.xSpeed
            self.rect.centery += self.ySpeed
            if self.dir:
                if self.xSpeed > 0:
                    self.xSpeed -= 1
                else:
                    self.xSpeed = 0
            else:
                if self.xSpeed < 0:
                    self.xSpeed += 1
                else:
                    self.xSpeed = 0

            if self.rect.centerx <= 0:
                self.rect.centerx = 0
            elif self.rect.centerx >= 800:
                self.rect.centerx = 800
                
            if self.rect.centery <= 560:
                self.ySpeed += self.gravity
            if self.rect.centery > 560:
                self.ySpeed = 0
                self.rect.centery = 560
                    
            if self.deleteTime >= 0:
                self.deleteTime -= 1
            else:
                self.pickedUp = True
        else:
            self.skipUpdate = False

    def getItemCondition(self):
        return [self.rect.centerx, self.rect.centery, self.pickedUp]
            
###################################
###################################
########### SPIKY BALLS  ###########
###################################
###################################
class spikyBalls(pygame.sprite.Sprite):
    def __init__(self,dropPos,dirFace):
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
        self.dirFace = dirFace
        self.ySpeed = 0
        if self.dirFace:
            self.xSpeed = 10
        else:
            self.xSpeed = -10
    
    def update(self):
        screen.blit(self.image, (self.rect.centerx, self.rect.centery))
        self.rect.centerx += self.xSpeed
        self.rect.centery += self.ySpeed
        
        if self.dirFace:
            if self.xSpeed > 0:
                self.xSpeed -= 1
            else:
                self.xSpeed = 0
        else:
            if self.xSpeed < 0:
                self.xSpeed += 1
            else:
                self.xSpeed = 0

        if self.rect.centerx <= 0:
            self.rect.centerx = 0
        elif self.rect.centerx >= 800:
            self.rect.centerx = 800
        
        if self.rect.centery <= 560:
            self.ySpeed += self.gravity
        if self.rect.centery > 560:
            self.ySpeed = 0
            self.rect.centery = 560
        
        if self.deleteTime >= 0:
            self.deleteTime -= 1
        else:
            self.touched = True

    def getSpikyBallCondition(self):
        return (self.rect.centerx, self.rect.centery, self.touched)

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
        self.originalY = self.rect.centery
        self.targetx, self.targety = float(aimPos[0]), float(aimPos[1])
        self.speed = 10.0
        self.start = vector(self.rect.centerx, self.rect.centery)
        self.end = vector(self.targetx, self.targety)
        self.distance = self.end - self.start
        self.dir = self.distance.normalize()
        self.hit = False
                                
    def getBulletCondition(self):
        return (self.rect.centerx, self.rect.centery, self.hit)
    
    def update(self):
        screen.blit(self.image,(self.rect.centerx, self.rect.centery))
        """
        radians = math.atan2(self.start[0],self.end[1])
        radians += ((random.random() * 0) * random.choice([-1,1]))
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
        self.items = ["Items:","[    ]","[    ]","[    ]"]
        self.helthPoint = 200
        self.line = 0
        self.col = 1
        self.needUpdate = True
        self.selected = False
        pygame.mixer.init()
        pygame.mixer.fadeout(0)
        
        self.snd_menuOptionChosen = pygame.mixer.Sound("Sounds/menus/menu_option_chosen.ogg")
        self.snd_blip = pygame.mixer.Sound("Sounds/menus/menu_smallBit.ogg")

    def keyRead(self):                
        keys = pygame.key.get_pressed()
        pygame.time.delay(100)
        if keys[K_UP]:
            self.snd_blip.play()
            self.needUpdate = True
            self.line = (self.line -1)%2
            self.selected = False
        if keys[K_RIGHT]:
            self.snd_blip.play()
            self.selected = False
            self.needUpdate = True
            self.col = (self.col+1)% 3
        elif keys[K_LEFT]:
            self.snd_blip.play()
            self.selected = False
            self.needUpdate = True
            self.col = (self.col-1)%3
        elif keys[K_DOWN]:
            self.snd_blip.play()
            self.selected = False
            self.needUpdate = True
            self.line = (self.line+1)%2
        elif keys[K_RETURN]:
            self.snd_menuOptionChosen.play()
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

            if len(self.items) > 4:
                self.itemsCombine()
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
            
    def itemsCombine(self):
        newList = ["Items:"]
        for i in self.items:
            if i not in newList and i != "[    ]":
                newList+= [i]
        if len(newList) < 4:
            newList += (4-len(newList))*["[    ]"]
        self.items = newList
                

            
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
        
    def update(self):
        screen.blit(self.image, (0,575))

###################################
###################################
########### BLOCKS  ###############
###################################
###################################
class blocks(pygame.sprite.Sprite):
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load("Image/floor_block.png")
        self.rect = self.image.get_rect()
        # sets the floor randomely within a range
        self.rect.centerx = int(round((random.randrange(20, 700))/50)*50)
        self.rect.centery = int(round((random.randrange(300, 550))/50)*50)
        
    def update(self):
        screen.blit(self.image, (self.rect.centerx, self.rect.centery))
    #Sets the floor position
    def set(self, xPos, yPos):
        self.rect.centerx = xPos
        self.rect.centery = yPos


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
    healthRegenCoolDown = 500
    
    groundFloor = FloorLong()
    block1 = blocks()
    block2 = blocks()
    block3 = blocks()
    block4 = blocks()
    block1.set(30,500)
    terrain = pygame.sprite.OrderedUpdates(groundFloor,block1,block2,block3,block4)
    for floor in terrain:
            for overlap in terrain:
                if floor.rect.colliderect(overlap):
                    if not floor is overlap or (floor.rect.centery < 450 and floor.rect.centery > 399):
                        overlap.set(int(round((random.randrange(300, 500))/50)*50), 550)
                                              
    you = player()
    playerSprite = pygame.sprite.OrderedUpdates(you)
    
    menu = pausedMenu()

    pygame.mixer.init()
    snd_lvl0Music = pygame.mixer.Sound("Sounds/Levels/lvl_0.ogg")
    snd_lvl0Music.play(-1)

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
        
    itemsList = pygame.sprite.Group()
    itemSprites = pygame.sprite.OrderedUpdates(itemsList)
    
    monsterList = pygame.sprite.Group(monster1,monster2,monster3)
    enemySprites = pygame.sprite.OrderedUpdates(monsterList)

    allSprites = pygame.sprite.OrderedUpdates(monsterList,you)
    
    pygame.mouse.set_visible(False)
    while not gameOver:
        if Paused:
            menu.update(True)
            itemSelected = menu.getItemSelected()
            
        elif not Paused:
            
            playerCond = you.getPlayerCondition()

            spikyBallList = you.getSpikyBallList()
            bulletList = you.getBulletList()
            
            for sprite in allSprites:
                sprite.fall(terrain)

            for m in monsterList:
                monsterCond = m.getMonsterCondition()
                # check collision
                if pygame.sprite.collide_rect(you,m) and\
                   not monsterCond[2]:
                    healthRegenCoolDown = 500
                    monsterTouch = True
                    skipPlayerUpdate =True
                    playerSprite.update(monsterTouch, itemSelected, healthRegenCoolDown)
                else:
                    if not playerCond[2]:
                        healthRegenCoolDown -= 1
                    else:
                        healthRegenCoolDown = 500
                    monsterTouch = False

                if itemSelected == "axe":
                    attackingCond = you.getAttackingCondition()
                    if attackingCond[2]:
                        # This one I think use own identified colliditon is better
                        if -32<= attackingCond[0] - monsterCond[0] <= 32 and\
                           -32<= attackingCond[1] - monsterCond[1] <= 32:
                            m.monsterHit += 20
                            
                elif itemSelected == "spiky balls":
                    for s in spikyBallList:
                        if pygame.sprite.collide_rect(s,m):
                                m.monsterHit += 1
                                s.touched =True
                                
                elif itemSelected == "rifle":
                    if not m.die:
                        for b in bulletList:
                            if pygame.sprite.collide_rect(b,m):
                                m.monsterHit += 1
                                b.hit = True
                                
                elif itemSelected == "invincible":
                    if "invincible" in menu.items:
                        menu.items.remove("invincible")
                        
                if monsterCond[2] == True:
                    if rebornTime >= 0:
                        rebornTime -= 1
                    else:
                        rebornTime = reset
                        monsterList.remove(m)
                        newOne = monster(random.randint(100,700))
                        monsterList.add(newOne)
                        enemySprites = pygame.sprite.OrderedUpdates(monsterList)
                        
                if monsterCond[3]:
                    drop = items(monsterCond[0],monsterCond[1])
                    m.dropitems = False
                    itemsList.add(drop)
                    itemSprites = pygame.sprite.OrderedUpdates(itemsList)
                    
            if skipPlayerUpdate:
                skipPlayerUpdate = False
            else:
                playerSprite.update(monsterTouch, itemSelected, healthRegenCoolDown)

            if len(itemsList) > 0:
                itemSprites.update()
                for item in itemsList:
                    itemCond = item.getItemCondition()
                    if not itemCond[2]:
                        if pygame.sprite.collide_rect(item,you):
                            item.pickedUp = True
                            menu.items += [item.name[6:len(item.name)-4]]
                    else:
                        itemsList.remove(item)
                    
                
            enemySprites.update(playerCond)
            terrain.update()
            
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
    
    if not pygame.mixer:
            print("problem with sound")
    else:
        pygame.mixer.init()
        snd_menuOptionChosen = pygame.mixer.Sound("Sounds/menus/menu_option_chosen.ogg")
        snd_gameOverMusic = pygame.mixer.Sound("Sounds/menus/game_over.ogg")
        snd_blip = pygame.mixer.Sound("Sounds/menus/menu_smallBit.ogg")
        snd_introMusic = pygame.mixer.Sound("Sounds/menus/menu_music.ogg")
        snd_introMusic.play(-1) 

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
                    snd_menuOptionChosen.play()
                    notStarting = False
                if event.key == pygame.K_RIGHT:
                    snd_blip.play()
                    difficulty = (difficulty + 1)%3
                elif event.key == pygame.K_LEFT:
                    snd_blip.play()
                    difficulty = (difficulty - 1) % 3
                
        menuDisplay(difficulty)
        pygame.display.update()
    
    if not notStarting:
        run(difficulty)
      
    if gameOver:
        pygame.mixer.fadeout(100)
        snd_gameOverMusic.play(-1)
        font = pygame.font.Font(None, 64)
        text = font.render("GAME OVER! press R to restart",1,(0,0,0))
        textpos = text.get_rect()
        textpos.centerx = screen.get_rect().centerx
        textpos.centery = screen.get_rect().centery
        
        screen.blit(text,textpos)   
        pygame.display.update()
        while True:
            for event in pygame.event.get():
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_r :
                        snd_blip.play()
                        pygame.mixer.fadeout(3)
                        startingScreen()
                if event.type == QUIT:
                    pygame.quit()
                    sys.exit()


startingScreen()
