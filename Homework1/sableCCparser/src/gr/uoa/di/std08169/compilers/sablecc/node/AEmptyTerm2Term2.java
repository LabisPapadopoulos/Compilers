/* This file was generated by SableCC (http://www.sablecc.org/). */

package gr.uoa.di.std08169.compilers.sablecc.node;

import gr.uoa.di.std08169.compilers.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AEmptyTerm2Term2 extends PTerm2
{

    public AEmptyTerm2Term2()
    {
        // Constructor
    }

    @Override
    public Object clone()
    {
        return new AEmptyTerm2Term2();
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAEmptyTerm2Term2(this);
    }

    @Override
    public String toString()
    {
        return "";
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        throw new RuntimeException("Not a child.");
    }
}