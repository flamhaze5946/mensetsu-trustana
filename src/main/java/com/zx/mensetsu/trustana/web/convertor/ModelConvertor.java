package com.zx.mensetsu.trustana.web.convertor;

public interface ModelConvertor<TL, TU>
{
    TL convertToLower(TU upper);

    TU convertToUpper(TL lower);
}
