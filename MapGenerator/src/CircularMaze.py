'''
Created on Nov 20, 2011

@author: cyan
'''

import random

size = 21
direction = [[1, 0], [0, -1], [-1, 0], [0, 1]]
turnLeft = True

def isValid(x, y):
    if x >= 0 and x < size and y >= 0 and y < size:
        return True
    return False

if __name__ == '__main__':
    maze = [ [ 1 for i in range(size) ] for j in range(size) ]
    
    if turnLeft:
        direction.reverse()
    
    x = y = size / 2
    index = random.randint(0, 3)
    count = -1
    step = 0
    
    maze[x][y] = 3
    
    while isValid(x, y):
        count += 1
        if count % 2 == 0:
            step += 2
        index = (index + 1) % 4
        current_direction = direction[index]
        
        for i in range(step):
            x += current_direction[0]
            y += current_direction[1]
            if not isValid(x, y):                
                maze[x - current_direction[0]][y - current_direction[1]] = 2
                break
            maze[x][y] = 0
    
    for i in range(size):
        for j in range(size):
            print maze[i][j], #without newline
        print 
