/**
 * 
 * Copyright (c) 2001-2010, Purdue University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the Purdue University nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package immortal;

import realtime.AbsoluteTime;
import realtime.Clock;

public class NanoClock {

  public static final long baseMillis = 0;
  public static final int baseNanos = 0;


  public static AbsoluteTime roundUp( final AbsoluteTime t ) { // round up to next or second next period

     long tNanos = t.getNanoseconds();
     long tMillis = t.getMilliseconds();

     final long periodMillis = Constants.DETECTOR_PERIOD;
     
     if (tNanos > 0) {
       tNanos = 0;
       tMillis ++;
     }
     
     if ( periodMillis > 0 ) {
       tMillis = ((tMillis + periodMillis -1)/periodMillis) * periodMillis;
     }       
  
     return new AbsoluteTime( tMillis, (int)tNanos );
  }
  
  public static void init() {
    //    if (baseMillis != -1 || baseNanos != -1) {
    //      throw new RuntimeException("NanoClock already initialized.");
    //    }
    //    
    //    AbsoluteTime rt = roundUp(Clock.getRealtimeClock().getTime());
    //
    //    baseNanos = rt.getNanoseconds();
    //    baseMillis = rt.getMilliseconds();
  }

  public static long now() {
  
    final AbsoluteTime t = Clock.getRealtimeClock().getTime();  
    
    return convert(t);
  }
  
  public static long convert( final AbsoluteTime t ) {
  
    final long millis = t.getMilliseconds() - baseMillis;  
    final int nanos = t.getNanoseconds();
    
    return millis*1000000 + nanos - baseNanos;
  }
  
  public static String asString( final long relativeNanos ) {

    if (relativeNanos < 0) {
      if (relativeNanos == -1) {
        return "NA";
      }
    }
  
    long millis = baseMillis + relativeNanos / 1000000L ;
    int nanos = baseNanos + (int) ( relativeNanos % 1000000L );


    millis += nanos / 1000000L ;
    nanos = nanos % 1000000;  
    
    final String ns = Integer.toString(nanos);
    int zeros = 6-ns.length();
    StringBuffer result = new StringBuffer(Long.toString(millis));
    
    while(zeros-- >0) {
      result = result.append("0");
    }
    
    result = result.append(ns);    
    
    return result.toString();
  }

}