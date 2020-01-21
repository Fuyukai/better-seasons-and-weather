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

package green.sailor.mc.bsaw.api.entry;

import green.sailor.mc.bsaw.api.BiomeInfoMap;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for entry points that register new biome infos.
 */
public interface InfoRegistrator {
    /**
     * Implemented to add new entries to the biome info map.
     *
     * @param infoMap The {@link BiomeInfoMap} to register on.
     */
    void register(@NotNull BiomeInfoMap infoMap);
}
