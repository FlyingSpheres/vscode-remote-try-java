/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

package com.mycompany.app;

import javax.sound.sampled.SourceDataLine;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Remote World!" );
        long startTime = System.currentTimeMillis();
        try{
            Thread.sleep(2500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime-startTime)/1000.0);

    }
}
