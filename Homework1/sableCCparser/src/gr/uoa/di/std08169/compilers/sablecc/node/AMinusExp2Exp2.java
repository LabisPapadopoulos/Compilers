/* This file was generated by SableCC (http://www.sablecc.org/). */

package gr.uoa.di.std08169.compilers.sablecc.node;

import gr.uoa.di.std08169.compilers.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMinusExp2Exp2 extends PExp2
{
    private TMinus _minus_;
    private PTerm _term_;
    private PExp2 _exp2_;

    public AMinusExp2Exp2()
    {
        // Constructor
    }

    public AMinusExp2Exp2(
        @SuppressWarnings("hiding") TMinus _minus_,
        @SuppressWarnings("hiding") PTerm _term_,
        @SuppressWarnings("hiding") PExp2 _exp2_)
    {
        // Constructor
        setMinus(_minus_);

        setTerm(_term_);

        setExp2(_exp2_);

    }

    @Override
    public Object clone()
    {
        return new AMinusExp2Exp2(
            cloneNode(this._minus_),
            cloneNode(this._term_),
            cloneNode(this._exp2_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMinusExp2Exp2(this);
    }

    public TMinus getMinus()
    {
        return this._minus_;
    }

    public void setMinus(TMinus node)
    {
        if(this._minus_ != null)
        {
            this._minus_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._minus_ = node;
    }

    public PTerm getTerm()
    {
        return this._term_;
    }

    public void setTerm(PTerm node)
    {
        if(this._term_ != null)
        {
            this._term_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._term_ = node;
    }

    public PExp2 getExp2()
    {
        return this._exp2_;
    }

    public void setExp2(PExp2 node)
    {
        if(this._exp2_ != null)
        {
            this._exp2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._exp2_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._minus_)
            + toString(this._term_)
            + toString(this._exp2_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._minus_ == child)
        {
            this._minus_ = null;
            return;
        }

        if(this._term_ == child)
        {
            this._term_ = null;
            return;
        }

        if(this._exp2_ == child)
        {
            this._exp2_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._minus_ == oldChild)
        {
            setMinus((TMinus) newChild);
            return;
        }

        if(this._term_ == oldChild)
        {
            setTerm((PTerm) newChild);
            return;
        }

        if(this._exp2_ == oldChild)
        {
            setExp2((PExp2) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
