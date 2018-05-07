/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.core.event.domain.message;

import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Message;

import javax.annotation.Nullable;
import java.util.Optional;

public class MessageUpdateEvent extends MessageEvent {

    private final long messageId;
    private final long channelId;
    private final long guildId;

    @Nullable
    private final Message old;

    private final boolean contentChanged;
    @Nullable
    private final String currentContent;

    public MessageUpdateEvent(DiscordClient client, long messageId, long channelId, long guildId, @Nullable Message
            old, boolean contentChanged, @Nullable String currentContent) {
        super(client);
        this.messageId = messageId;
        this.channelId = channelId;
        this.guildId = guildId;
        this.old = old;
        this.contentChanged = contentChanged;
        this.currentContent = currentContent;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getChannelId() {
        return channelId;
    }

    public long getGuildId() {
        return guildId;
    }

    public Optional<Message> getOld() {
        return Optional.ofNullable(old);
    }

    public boolean isContentChanged() {
        return contentChanged;
    }

    public Optional<String> getCurrentContent() {
        return Optional.ofNullable(currentContent);
    }
}