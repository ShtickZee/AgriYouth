package com.wayne.ani_agad.data

data class CropInfo(
    val id: String,
    val name: String,
    val emoji: String,
    val daysToHarvest: String,
    val spacing: String,
    val recommendedPlot: String,
    val preferredMethod: String, // Field for autofill logic
    val howToWater: String,
    val waterNeeded: String,
    val commonPests: String,
    val howToGrow: String
)

object CropRepository {
    val allCrops = listOf(
        CropInfo(
            id = "pechay",
            name = "PECHAY (Pak Choi)",
            emoji = "🥬",
            daysToHarvest = "30 to 40 days from transplanting.",
            spacing = "15 cm x 15 cm spacing per plant.",
            recommendedPlot = "Hydroponics (UPLB SNAP / Static Kratky)",
            preferredMethod = "Passive Hydroponics (UPLB SNAP)",
            howToWater = "- Soil/Pot: 1 to 2 times a day (morning and late afternoon) depending on ambient heat.\n- Hydroponics: 0 times a day. (Static system. Do not water daily).",
            waterNeeded = "- Soil/Pot: 200ml to 300ml per watering (or until slight water drains from the bottom holes). Do not let the pot sit in a saucer of stagnant water.\n- Hydroponics: Initial fill up to 1 cm below the base of the net cup. The initial nutrient solution contains enough food for the entire 30-day cycle. Do not add more nutrients. If the water level drops below the roots, top up with plain tap water only.",
            commonPests = "- Flea beetles (tiny black bugs that chew small holes in leaves). Mitigation: Use fine insect netting.\n- Caterpillars / Leaf miners. Mitigation: Manual removal or application of neem oil.\n- Root rot (Pythium). Mitigation: Ensure the hydroponic air gap is maintained; do not submerge the net cup entirely.",
            howToGrow = "Best Method: Passive Hydroponics (The \"No-Pump\" Water Method / UPLB SNAP).\n\nThe Container: A 16oz to 32oz opaque (light-blocking) plastic cup or recycled coffee can. It must have a lid with a hole cut in the middle to hold the plant's net cup. *Rule: If light hits the water, green algae will grow, so use dark colors or wrap it in foil.*\n\nThe Setup & Watering:\n1. Mix your nutrient water and fill the container exactly 1 centimeter below the bottom of the plant's cup.\n2. Leave that 1cm \"air gap\" so the roots can breathe.\n3. Do not water daily. Just check it once a week. When the water level drops by half, top it up with plain tap water only.\n\nThe Food (Nutrients): UPLB SNAP Hydroponic Nutrients (Part A and Part B mixed with water). The initial mix has enough food for the entire 30-day grow. Do not add extra plant food later."
        ),
        CropInfo(
            id = "kangkong",
            name = "KANGKONG (Water Spinach)",
            emoji = "🌿",
            daysToHarvest = "20 to 25 days for the first harvest. Subsequent harvests every 10 to 14 days via ratoon cropping (cutting the top and letting side shoots grow).",
            spacing = "10 cm x 10 cm spacing. Can be grown densely.",
            recommendedPlot = "Hydroponics (Static Kratky / SNAP)",
            preferredMethod = "Passive Hydroponics (UPLB SNAP)",
            howToWater = "- Soil/Pot: 1 to 2 times a day. Kangkong requires a near-waterlogged environment.\n- Hydroponics: 0 times a day. (Static system).",
            waterNeeded = "- Soil/Pot: 300ml to 400ml per watering. The soil should feel like a wrung-out sponge, never completely dry.\n- Hydroponics: Initial fill to 1 cm below the net cup. Top up with plain tap water as the plant consumes it. Kangkong has high water uptake. Do not add extra nutrients.",
            commonPests = "- Aphids (cluster on the undersides of leaves and stem tips). Mitigation: Wash off with a gentle stream of water or use insecticidal soap.\n- Stem borers. Mitigation: Remove and destroy affected stems immediately.\n- Yellowing leaves. Mitigation: Usually a nitrogen deficiency; ensure the initial SNAP Nutrient Solution A and B were mixed in the correct equal ratio.",
            howToGrow = "Best Method: Passive Hydroponics (Kratky Method). Kangkong is a water-lover and thrives here.\n\nThe Container: A slightly larger opaque container, like a small ice cream tub or a 1-liter plastic bottle cut in half.\n\nThe Setup & Watering:\n1. Fill the nutrient water so it just barely touches the bottom of the plant's cup.\n2. Because kangkong drinks a lot of water, check it twice a week.\n3. When the water level drops, top it up with plain tap water.\n\nThe Food (Nutrients): UPLB SNAP Nutrient Solution. Mix the full recommended dose at the start. Since it's a heavy feeder, getting the initial mix right is crucial. No extra feeding needed after day 1."
        ),
        CropInfo(
            id = "mustasa",
            name = "MUSTASA (Indian Mustard / Leaf Mustard)",
            emoji = "🌱",
            daysToHarvest = "30 to 40 days from transplanting.",
            spacing = "20 cm x 20 cm spacing per plant (it grows slightly larger and more sprawling than pechay).",
            recommendedPlot = "Hydroponics (UPLB SNAP / Static Kratky)",
            preferredMethod = "Passive Hydroponics (UPLB SNAP)",
            howToWater = "- Soil/Pot: 1 time a day (preferably in the morning).\n- Hydroponics: 0 times a day. (Static system).",
            waterNeeded = "- Soil/Pot: 250ml to 300ml per watering (or until slight drainage). Allow the top 1 cm of soil to dry slightly before the next watering to prevent fungal issues.\n- Hydroponics: Initial fill to 1 cm below the net cup. Top up with plain tap water when the reservoir drops significantly. Do not add extra nutrients.",
            commonPests = "- Flea beetles and Aphids. Mitigation: Use yellow sticky traps and fine insect netting.\n- Downy mildew (yellow patches on top of leaves, fuzzy gray underneath). Mitigation: Ensure good air circulation between plants; avoid overhead watering in soil setups.",
            howToGrow = "Best Method: Passive Hydroponics (UPLB SNAP Method).\n\nThe Container: A 16oz to 32oz opaque plastic container with a net cup lid. Must block out all sunlight.\n\nThe Setup & Watering:\n1. Fill the container with nutrient water up to 1cm below the net cup.\n2. Leave the air gap for root breathing.\n3. Check weekly. When the water drops by half, top it up with plain tap water.\n\nThe Food (Nutrients): UPLB SNAP Nutrient Solution. Mix the initial batch according to the package. That single batch will feed the plant all the way to harvest day."
        ),
        CropInfo(
            id = "lettuce",
            name = "LETTUCE (Heat-Tolerant Varieties)",
            emoji = "🥗",
            daysToHarvest = "35 to 45 days from transplanting.",
            spacing = "20 cm x 20 cm spacing per plant.",
            recommendedPlot = "Hydroponics (UPLB SNAP / Static Kratky)",
            preferredMethod = "Passive Hydroponics (UPLB SNAP)",
            howToWater = "- Soil/Pot: 1 time a day (early morning only).\n- Hydroponics: 0 times a day. (Static system).",
            waterNeeded = "- Soil/Pot: 200ml to 250ml. Water only at the base of the plant. Wet leaves in humid PH weather invite rot.\n- Hydroponics: Initial fill to 1 cm below the net cup. Top up with plain tap water when the level drops. Do not add extra nutrients. Crucial: Keep the reservoir shaded; if the water temperature exceeds 30°C, the roots will rot.",
            commonPests = "- Tip burn (brown, crispy edges on inner leaves). Mitigation: This is a calcium deficiency caused by lack of transpiration. Ensure good air flow and avoid extreme heat.\n- Bolting (plant grows tall and goes to seed, turning bitter). Mitigation: Provide 30% shade net during the hottest part of the day (11 AM - 3 PM). Harvest immediately if a central stem starts shooting up.\n- Slugs and Snails. Mitigation: Manual removal at night.",
            howToGrow = "Best Method: Passive Hydroponics (UPLB SNAP Method).\n\nThe Container: A 16oz to 32oz opaque container. Pro-tip: Keep this container in the shade during the hot afternoon (12 PM - 3 PM) so the water doesn't get too warm and cook the roots.\n\nThe Setup & Watering:\n1. Fill with nutrient water to 1cm below the net cup.\n2. Maintain the air gap.\n3. Top up with plain tap water when the level drops by half.\n\nThe Food (Nutrients): UPLB SNAP Nutrient Solution. Mix the initial batch. No extra feeding required."
        ),
        CropInfo(
            id = "sibuyas",
            name = "SIBUYAS DAHON (Spring Onion)",
            emoji = "🧅",
            daysToHarvest = "30 to 45 days from planting seeds/bulbs. (If regrowing from kitchen scraps, 10-14 days).",
            spacing = "5 cm x 5 cm spacing. Can be planted very densely.",
            recommendedPlot = "Container Potting (Soil)",
            preferredMethod = "Container Potting (Soil)",
            howToWater = "- Soil/Pot: Every 1 to 2 days. (Do not water daily if the soil is still wet).\n- Hydroponics: 0 times a day. (Static system).",
            waterNeeded = "- Soil/Pot: 150ml to 200ml. Onions are highly susceptible to rot. Water only when the top 2 inches of soil feel dry to the touch.\n- Hydroponics: Initial fill so only the very bottom roots touch the water. Top up with plain tap water as needed.",
            commonPests = "- Onion thrips (tiny, slender insects that cause silvery/white streaks on leaves). Mitigation: Spray with a mild soap solution or neem oil in the late afternoon.\n- Bulb/Stem rot. Mitigation: Caused by overwatering or poor drainage. Ensure pots have adequate drainage holes and do not let the soil become waterlogged.",
            howToGrow = "Best Method: Soil Potting. (Onions need a physical medium to form their white bulbs, making soil much easier than water for beginners).\n\nThe Container: A long rectangular planter or a deep 6-inch pot. Must have drainage holes at the bottom.\n\nThe Setup & Watering:\n1. Fill the pot with a fluffy soil mix. Plant the bulbs or seeds about 1 inch deep.\n2. Use the \"Knuckle Test\" to water: stick your finger an inch into the soil. If it’s dry, water it. If it’s wet, wait. (Usually every 1 to 2 days).\n3. Water slowly at the base until a little water drips out the bottom holes, then empty the saucer underneath.\n\nThe Food (Nutrients): Mix a handful of vermicast (worm castings) into your soil before planting. That provides all the slow-release food the onions need for 30 days. No liquid fertilizers needed."
        ),
        CropInfo(
            id = "sili",
            name = "SILI (Chili Pepper)",
            emoji = "🌶️",
            daysToHarvest = "70 to 90 days from transplanting seedlings.",
            spacing = "1 plant per 1-gallon (4-liter) pot minimum. (Note: 3 to 5-gallon pots are strongly recommended for maximum fruit yield, but 1-gallon is the minimum for apartments).",
            recommendedPlot = "Container Potting (Soil)",
            preferredMethod = "Container Potting (Soil)",
            howToWater = "- Soil/Pot: Every 1 to 2 days. (Allow the soil to dry out slightly between waterings to prevent root rot and encourage fruiting).\n- Hydroponics: 0 times a day. (Active system requires pump maintenance, not daily manual watering).",
            waterNeeded = "- Soil/Pot: 300ml to 500ml per watering. Water deeply until it runs out the bottom, then wait until the top 1-2 inches of soil feel completely dry before watering again.\n- Hydroponics: Maintain reservoir levels according to the specific active system's guidelines.",
            commonPests = "- Aphids and Spider Mites (often found under leaves). Mitigation: Wipe leaves with a damp cloth or use a strong jet of water to dislodge them.\n- Flower drop (flowers fall off before fruiting). Mitigation: Usually caused by extreme heat, overwatering, or lack of pollination. Gently shake the plant branches during flowering to help distribute pollen. Ensure soil is not too wet.\n- Anthracnose (fruit rot with dark, sunken lesions). Mitigation: Remove infected fruits immediately. Ensure plants are not overcrowded to allow air circulation.",
            howToGrow = "Best Method: Soil Potting. (Fruiting plants need a large volume of soil to support their heavy roots and branches).\n\nThe Container: A 1-gallon to 3-gallon pot (about the size of a standard bucket). Must have drainage holes.\n\nThe Setup & Watering:\n1. Fill with a fluffy soil mix and plant your seedling.\n2. Water deeply until it drips out the bottom.\n3. Crucial step: Wait until the top 2 inches of soil feel completely dry to the touch before watering again (usually every 2 to 3 days). Peppers hate \"wet feet\" and will drop their flowers if the soil is too soggy.\n\nThe Food (Nutrients): Mix vermicast into the soil at planting. Once the plant starts producing little white flowers, sprinkle a teaspoon of standard complete fertilizer (like 14-14-14) around the edges of the pot to help it grow big chilies."
        )
    )
}
