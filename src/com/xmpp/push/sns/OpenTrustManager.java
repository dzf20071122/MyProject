/**
 * $RCSfile$
 * $Revision: $
 * $Date: $
 *
 * Copyright 2003-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmpp.push.sns;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Dummy trust manager that trust all certificates presented by the server. This class
 * is used during old SSL connections.
 *
 * @author Gaston Dombiak
 */
class OpenTrustManager implements X509TrustManager {

    public OpenTrustManager() {
    }

    @Override
	public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    @Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }
}