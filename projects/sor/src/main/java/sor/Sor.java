package sor;

/*
 * Copyright (C) 2000 by ETHZ/INF/CS
 * All rights reserved
 * 
 * @version $Id$
 * @author Florian Schneider
 */

//import sor.EDU.oswego.cs.dl.util.concurrent.CyclicBarrier;

public class Sor
{
  
  public final int N = 5;
  public final int M = 5;
  public final int iterations = 2;


  public float[][] black_ = new float[M + 2][N + 1];
  public float[][] red_ = new float[M + 2][N + 1];
  
  public int nprocs = 2;
  public SimpleBarrier barrier;
  static Thread[] t;
  
  public void go(final int numThreads)
  {
    nprocs = numThreads;
    t = new Thread[nprocs];
    barrier = new SimpleBarrier(nprocs);
    
    // initialize arrays
    int first_row = 1;
    int last_row = M;
    
    for (int i = first_row; i <= last_row; i++)
    {
      /*
       * Initialize the top edge.
       */
      if (i == 1)
      {
        for (int j = 0; j <= N; j++)
        {
          red_[0][j] = black_[0][j] = (float) 1.0;
        }
      }
      /*
       * Initialize the left and right edges.
       */
      if ((i & 1) != 0)
      {
        red_[i][0] = (float) 1.0;
        black_[i][N] = (float) 1.0;
      }
      else
      {
        black_[i][0] = (float) 1.0;
        red_[i][N] = (float) 1.0;
      }
      /*
       * Initialize the bottom edge.
       */
      if (i == M)
      {
        for (int j = 0; j <= N; j++)
        {
          red_[i + 1][j] = black_[i + 1][j] = (float) 1.0;
        }
      }
    }
    
    
    // start computation
    
    
    for (int proc_id = 0; proc_id < nprocs; proc_id++)
    {
      first_row = (M * proc_id) / nprocs + 1;
      last_row = (M * (proc_id + 1)) / nprocs;
      
      if ((first_row & 1) != 0)
      {
        t[proc_id] =
            new sor_first_row_odd(
                first_row,
                last_row,
                N,
                M,
                black_,
                red_,
                iterations,
                barrier);
      }
      else
      {
        t[proc_id] =
            new sor_first_row_even(
                first_row,
                last_row,
                N,
                M,
                black_,
                red_,
                iterations,
                barrier);
      }
      t[proc_id].start();
    }
    
    for (int proc_id = 0; proc_id < nprocs; proc_id++)
    {
      try
      {
        t[proc_id].join();
      }
      catch (final InterruptedException e)
      {
      }
    }
    
    
    // print out results
    float red_sum = 0, black_sum = 0;
    for (int i = 0; i < M + 2; i++)
    {
      for (int j = 0; j < N + 1; j++)
      {
        red_sum += red_[i][j];
        black_sum += black_[i][j];
      }
    }
    System.out.println("Exiting. red_sum = "
        + red_sum
        + ", black_sum = "
        + black_sum);
  }
  
  public static void print(final String s)
  {
    System.out.println(Thread.currentThread().getName() + ":" + s);
  }

}


class sor_first_row_odd extends Thread
{
  
  int first_row;
  int end;
  int N;
  int M;
  float[][] black_;
  float[][] red_;
  int iterations;
  SimpleBarrier barrier;
  
  
  public sor_first_row_odd(final int a, final int b, final int N, final int M,
      final float[][] black_, final float[][] red_, final int iterations,
      final SimpleBarrier barrier)
  {
    first_row = a;
    end = b;
    this.N = N;
    this.M = M;
    this.black_ = black_;
    this.red_ = red_;
    this.iterations = iterations;
    this.barrier = barrier;
  }
  
  @Override
  public void run()
  {
    int i, j, k;
    
    for (i = 0; i < iterations; i++)
    {
      //print("iteration A "+i);
      for (j = first_row; j <= end; j++)
      {
        
        for (k = 0; k < N; k++)
        {
          
          black_[j][k] =
              (red_[j - 1][k] + red_[j + 1][k] + red_[j][k] + red_[j][k + 1])
                  / (float) 4.0;
        }
        if ((j += 1) > end)
        {
          break;
        }
        
        for (k = 1; k <= N; k++)
        {
          
          black_[j][k] =
              (red_[j - 1][k] + red_[j + 1][k] + red_[j][k - 1] + red_[j][k])
                  / (float) 4.0;
        }
      }
      try
      {
        //print("barrier 1a - "+System.currentTimeMillis());
        barrier.barrier();
      }
      catch (final InterruptedException e)
      {
      }
      
      for (j = first_row; j <= end; j++)
      {
        
        for (k = 1; k <= N; k++)
        {
          
          red_[j][k] =
              (black_[j - 1][k] + black_[j + 1][k] + black_[j][k - 1] + black_[j][k])
                  / (float) 4.0;
        }
        if ((j += 1) > end)
        {
          break;
        }
        
        for (k = 0; k < N; k++)
        {
          
          red_[j][k] =
              (black_[j - 1][k] + black_[j + 1][k] + black_[j][k] + black_[j][k + 1])
                  / (float) 4.0;
        }
      }
      try
      {
        //print("barrier 2a - "+System.currentTimeMillis());
        barrier.barrier();
      }
      catch (final InterruptedException e)
      {
      }

    }
    
  }

}

class sor_first_row_even extends Thread
{
  
  int first_row;
  int end;
  int N;
  int M;
  float[][] black_;
  float[][] red_;
  int iterations;
  SimpleBarrier barrier;
  
  public sor_first_row_even(final int a, final int b, final int N, final int M,
      final float[][] black_, final float[][] red_, final int iterations,
      final SimpleBarrier barrier)
  {
    first_row = a;
    end = b;
    this.N = N;
    this.M = M;
    this.black_ = black_;
    this.red_ = red_;
    this.iterations = iterations;
    this.barrier = barrier;
  }
  
  @Override
  public void run()
  {
    int i, j, k;
    
    for (i = 0; i < iterations; i++)
    {
      //print("iteration B "+i);	    
      for (j = first_row; j <= end; j++)
      {
        
        for (k = 1; k <= N; k++)
        {
          
          black_[j][k] =
              (red_[j - 1][k] + red_[j + 1][k] + red_[j][k - 1] + red_[j][k])
                  / (float) 4.0;
        }
        if ((j += 1) > end)
        {
          break;
        }
        
        for (k = 0; k < N; k++)
        {
          
          black_[j][k] =
              (red_[j - 1][k] + red_[j + 1][k] + red_[j][k] + red_[j][k + 1])
                  / (float) 4.0;
        }
      }
      try
      {
        //print("barrier 1b - "+System.currentTimeMillis());
        barrier.barrier();
      }
      catch (final InterruptedException e)
      {
      }
      
      for (j = first_row; j <= end; j++)
      {
        
        for (k = 0; k < N; k++)
        {
          
          red_[j][k] =
              (black_[j - 1][k] + black_[j + 1][k] + black_[j][k] + black_[j][k + 1])
                  / (float) 4.0;
        }
        if ((j += 1) > end)
        {
          break;
        }
        
        for (k = 1; k <= N; k++)
        {
          
          red_[j][k] =
              (black_[j - 1][k] + black_[j + 1][k] + black_[j][k - 1] + black_[j][k])
                  / (float) 4.0;
        }
      }
      try
      {
        //print("barrier 2b - "+System.currentTimeMillis());
        barrier.barrier();
      }
      catch (final InterruptedException e)
      {
      }

    }
  }
}
