'''
Created on Nov 20, 2011

@author: cyan
'''

import random

width = 21
height = 8

if __name__ == '__main__':
    maze = [ [ 0 for i in range(height) ] for j in range(width) ]
    
    for i in range(1, width, 2):
        opening = random.randint(0, height-1)
        for j in range(height):
            if j != opening:
                maze[i][j] = 1
                
    maze[0][height-1] = 3
    maze[width-1][0] = 2
    
    for j in range(height):
        for i in range(width):
            print maze[i][j], #without newline
        print 
