/**
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
package com.xmpp.push.sns.packet;

import com.xmpp.push.sns.Connection;
import com.xmpp.push.sns.PacketCollector;
import com.xmpp.push.sns.SmackConfiguration;
import com.xmpp.push.sns.XMPPException;
import com.xmpp.push.sns.filter.PacketFilter;
import com.xmpp.push.sns.filter.PacketIDFilter;

/**
 * Utility class for doing synchronous calls to the server.  Provides several
 * methods for sending a packet to the server and waiting for the reply.
 * 
 * @author Robin Collier
 */
final public class SyncPacketSend
{
	private SyncPacketSend()
	{	}
	
	static public Packet getReply(Connection connection, Packet packet, long timeout)
		throws XMPPException
	{
        PacketFilter responseFilter = new PacketIDFilter(packet.getPacketID());
        PacketCollector response = connection.createPacketCollector(responseFilter);
        
        connection.sendPacket(packet);

        // Wait up to a certain number of seconds for a reply.
        Packet result = response.nextResult(timeout);

        // Stop queuing results
        response.cancel();

        if (result == null) {
            throw new XMPPException("No response from server.");
        }
        else if (result.getError() != null) {
            throw new XMPPException(result.getError());
        }
        return result;
	}

	static public Packet getReply(Connection connection, Packet packet)
		throws XMPPException
	{
		return getReply(connection, packet, SmackConfiguration.getPacketReplyTimeout());
	}
}
