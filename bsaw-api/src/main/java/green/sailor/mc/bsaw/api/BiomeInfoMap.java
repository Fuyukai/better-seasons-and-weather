/*
 * This file is part of BSAW-API.
 *
 * BSAW is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BSAW is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BSAW.  If not, see <https://www.gnu.org/licenses/>.
 */

package green.sailor.mc.bsaw.api;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the mapping used for biome information by BSAW.
 */
public interface BiomeInfoMap {
    /** The default unknown extended info. */
    BiomeExtendedInfo DEFAULT = new BiomeExtendedInfo("bsaw:unknown", 20.0, 21.0);

    /**
     * Adds biome information to the map.
     *
     * @param identifier The identifier for the biome to add.
     * @param info The extended information for the biome.
     */
    void add(@NotNull String identifier, @NotNull BiomeExtendedInfo info);

    /**
     * Adds biome information to the map. The identifier will be extracted from the info.
     *
     * @param info The extended information for the biome.
     */
    void add(@NotNull BiomeExtendedInfo info);

    /**
     * Gets extended biome information.
     *
     * @param identifier The identifier of the biome.
     * @return A {@link BiomeExtendedInfo}, or the default info if not registered.
     */
    @NotNull BiomeExtendedInfo get(@NotNull String identifier);
}
