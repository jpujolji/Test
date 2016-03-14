/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test.interfaces;

import org.json.JSONObject;

public interface HttpInterface {

    void onProgress(long progress);

    void onSuccess(JSONObject response);

    void onFailed(JSONObject errorResponse);

}
