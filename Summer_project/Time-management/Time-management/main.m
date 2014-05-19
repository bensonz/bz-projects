//
//  main.m
//  Time-management
//
//  Created by Bz on 5/18/14.
//  Copyright (c) 2014 Bz. All rights reserved.
//

#import <Cocoa/Cocoa.h>

// main.m
#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        double odometer = 9200.8;
        int odometerAsInteger = (int)odometer;
        
        NSLog(@"You've driven %.1f miles", odometer);        // 9200.8
        NSLog(@"You've driven %d miles", odometerAsInteger); // 9200
    }
    return 0;
}