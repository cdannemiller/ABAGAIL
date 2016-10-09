package util;

import java.lang.reflect.Field;
import java.util.Random;

public class RandomProvider {
    private static Random random = new Random();

    public static void setSeed(int seed) throws NoSuchFieldException, IllegalAccessException
    {
      random = new Random(seed);
    }
    
    public static Random get()
    {
      return new Random(random.nextInt());
    }
}