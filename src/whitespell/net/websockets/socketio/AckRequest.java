/**
 * Copyright 2012 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package whitespell.net.websockets.socketio;

import whitespell.net.websockets.socketio.parser.Packet;
import whitespell.net.websockets.socketio.parser.PacketType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ack request received from Socket.IO client.
 * You can always check is it <code>true<code> through
 * {@link #isAckRequested()} method.
 *
 * You can call {@link #sendAckData} methods only during
 * {@link whitespell.net.websockets.socketio.listener.DataListener#onData} invocation. If {@link #sendAckData}
 * not called it will be invoked with empty arguments right after
 * {@link whitespell.net.websockets.socketio.listener.DataListener#onData} method execution by server.
 *
 * This object is NOT actual anymore if {@link #sendAckData} was
 * executed or {@link whitespell.net.websockets.socketio.listener.DataListener#onData} invocation finished.
 *
 */
public class AckRequest {

    private final Packet originalPacket;
    private final SocketIOClient client;
    private final AtomicBoolean sended = new AtomicBoolean();

    public AckRequest(Packet originalPacket, SocketIOClient client) {
        this.originalPacket = originalPacket;
        this.client = client;
    }

    /**
     * Check whether ack request was made
     *
     * @return true if ack requested by client
     */
    public boolean isAckRequested() {
        return originalPacket.isAck();
    }

    /**
     * Send ack data to client.
     * Can be invoked only once during {@link whitespell.net.websockets.socketio.listener.DataListener#onData}
     * method invocation.
     *
     * @param objs - ack data objects
     */
    public void sendAckData(Object... objs) {
        List<Object> args = Arrays.asList(objs);
        sendAckData(args);
    }

    /**
     * Send ack data to client.
     * Can be invoked only once during {@link whitespell.net.websockets.socketio.listener.DataListener#onData}
     * method invocation.
     *
     * @param objs - ack data object list
     */
    public void sendAckData(List<Object> objs) {
        if (!isAckRequested() || !sended.compareAndSet(false, true)) {
            return;
        }
        Packet ackPacket = new Packet(PacketType.ACK);
        ackPacket.setAckId(originalPacket.getId());
        ackPacket.setArgs(objs);
        client.send(ackPacket);
    }

}
