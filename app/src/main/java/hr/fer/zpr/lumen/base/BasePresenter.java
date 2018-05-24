package hr.fer.zpr.lumen.base;

import java.lang.ref.WeakReference;

import javax.inject.Inject;


public abstract class BasePresenter<View> implements ScopedPresenter {



    private WeakReference<View> viewReference=new WeakReference<>(null);

    public BasePresenter(final View view){
        viewReference=new WeakReference<>(view);
    }

}
