package org.codevillage;

/**
 * A class that represents a pair of integers
 */
public class IntPair {
    public int First, Second;
    public IntPair(int first, int second)
    {
        this.First = first;
        this.Second = second;
    }

    /**
     * Two pairs are equal if their two numbers are equal
     * @param o The thing to compare against
     * @return Whether the other object is an IntPair and their two integer's are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPair))
            return false;

        IntPair other = (IntPair) o;
        return First == other.First && Second == other.Second;
    }

    @Override
    public int hashCode()
    {
        return 31 * First + Second;
    }

    public String toString()
    {
        return "(" + First + ", " + Second + ")";
    }
}
