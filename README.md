### Plugin Documentation: Lumia

#### Overview
Lumia is a Minecraft plugin designed to manage and enhance player skills. It provides a robust system for tracking skill progression, levels, XP, tiers, and ranks. The plugin integrates with PlaceholderAPI to allow server administrators to display skill-related information dynamically.

---

### Features
1. **Skill Management**:
    - Tracks player skills such as Farming, Combat, Mining, and more.
    - Handles XP addition, level-up logic, and tier/rank calculations.
    - Provides a GUI menu to display skill progress.

2. **Database Integration**:
    - Uses SQLite for persistent storage of player skill data.

3. **PlaceholderAPI Integration**:
    - Exposes placeholders for skill-related data.

4. **Customizable Configuration**:
    - Language and skill settings are configurable via `config.yml` and `languages.yml`.

---

### Commands
#### `/lumia skills`
- **Description**: Opens a GUI menu displaying all player skills.
- **Permission**: `lumia.skills`

---

### Placeholders (PlaceholderAPI Integration)
The plugin integrates with PlaceholderAPI to provide the following placeholders:

| Placeholder                  | Description                                                                 |
|------------------------------|-----------------------------------------------------------------------------|
| `%lumia_level_<skill>%`      | Displays the current level of the specified skill.                         |
| `%lumia_xp_<skill>%`         | Displays the current XP of the specified skill.                           |
| `%lumia_tier_<skill>%`       | Displays the tier of the specified skill.                                 |
| `%lumia_rank_<skill>%`       | Displays the rank of the specified skill.                                 |
| `%lumia_name_<skill>%`       | Displays the display name of the specified skill.                         |
| `%lumia_progress_<skill>%`   | Displays the progress percentage towards the next level of the skill.      |

**Example Usage**:
- `%lumia_level_farming%` → Displays the Farming skill level.
- `%lumia_xp_combat%` → Displays the current XP for the Combat skill.

---

### Configuration
#### `config.yml`
- **`language`**: Sets the plugin's language (e.g., `en`, `es`).
- **Skill Settings**:
    - `base-xp`: Base XP required for leveling up.
    - `progression`: Multiplier for XP progression.
    - `xp-per-action`: XP gained per action (e.g., mining ores).

#### Example:
```yaml
language: "en"
skill:
  farming:
    base-xp: 100
    progression: 1.4
    xp-per-action: 5
```

#### `languages.yml`
- Contains localized messages for the plugin.
- Example:
```yaml
messages:
  en:
    prefix: "[Lumia]"
    progress_to_level: "Progress to level {level}"
    no_perks: "No perks available"
```

---

### Skill System
#### Supported Skills:
- Farming
- Combat
- Mining
- Foraging
- Fishing
- Enchanting
- Alchemy
- Archery
- Agility
- Smithing

#### Skill Properties:
- **Level**: Tracks the player's current level in the skill.
- **XP**: Tracks the player's current XP in the skill.
- **Tier**: Calculated based on the level (e.g., every 10 levels = 1 tier).
- **Rank**: A string representation of the skill's tier and type.

---

### GUI Menu
- **Command**: `/lumia skills`
- **Description**: Displays a menu with all skills, their levels, XP, and progress.
- **Icons**:
    - Farming → Wheat
    - Combat → Diamond Sword
    - Mining → Iron Pickaxe
    - Foraging → Oak Log
    - Fishing → Fishing Rod
    - Enchanting → Enchanting Table
    - Alchemy → Brewing Stand
    - Archery → Bow
    - Agility → Feather
    - Smithing → Smithing Table

---

### Database
- **Storage**: SQLite
- **Tables**:
    - `player_skills`: Stores player UUID, skill type, XP, and level.

---

### Integration
#### PlaceholderAPI
- **Setup**:
    1. Install PlaceholderAPI on your server.
    2. Ensure Lumia is installed and enabled.
    3. Use the placeholders listed above in your server's configuration files or plugins.

---

### Developer Notes
- **SkillManager**: Handles loading, saving, and managing player skills.
- **SQLStorage**: Manages database operations for skill data.
- **LanguageConfig**: Handles language-specific messages.
- **SkillType**: Enum representing all skill types.
- **AbstractSkill**: Base class for all skills.
- **PlayerSkillHolder**: Manages all skills for a specific player.

---

This documentation provides a comprehensive guide to using and configuring the Lumia plugin.