package mesa.app.utils;

public interface DoubleParamFunc<U, V, T> {
	T execute(U u, V v);
}
