package com.wayne.ani_agad.data

data class HydroLayoutInfo(
    val id: String,
    val title: String,
    val description: String,
    val instructions: String
)

data class GrowthMethodInfo(
    val id: String,
    val title: String,
    val intro: String,
    val manual: String
)

object GrowthMethodRepository {
    val hydroIntro = """
        What It Is: Growing plants in nutrient-rich water without electricity or pumps. 
        *Note: UPLB SNAP is the specific nutrient recipe and technique used here. The standard UPLB SNAP setup is Horizontal (Tabletop), but you can use the exact same nutrient rules for Vertical or Hanging setups.*

        Layout Styles:
        • Horizontal / Tabletop: Containers laid out in rows (The classic UPLB SNAP setup).
        • Vertical / Tower: Stacked containers saving floor space.
        • Hanging: Bottles suspended from balcony ceilings or rails.

        Core Materials & Nutrient Measurements (Applies to all layouts):
        • The Reservoir: Any opaque (light-blocking) container. 
        • The Net Cup: 2-inch or 3-inch plastic mesh cups that hold the plant.
        • Growing Media: 1 handful of Carbonized Rice Hull (CRH) or clean pebbles per cup to hold the stem upright.
        • Nutrients (UPLB SNAP Part A & B): Measurement: Mix exactly 2ml of Part A and 2ml of Part B per 1 Liter of water. Always mix Part A into the water first, stir, then add Part B.

        Basic Setup & Maintenance Rules (Applies to all layouts):
        1. The 1cm Rule: Always fill your container with nutrient water so it stops exactly 1 cm below the bottom of the net cup. 
        2. The Air Gap: That 1cm empty space is mandatory. The roots drink from the water, but the top roots need that air gap to breathe oxygen.
        3. Maintenance: Check weekly. When the water level drops by 50%, top it up with plain tap water only. Do not add more nutrients.

        Where to Buy & Alternatives:
        • Where to Buy: Search "UPLB SNAP Hydroponics", "Net cup", and "Hydroponic net cup" on Shopee or Lazada. Local plant nurseries and hardware stores (Ace, Handyman) also carry them.
        • No Net Cup? Take a small plastic drinking cup, poke 10-15 small holes in the bottom and sides, and use that instead.
        • No Growing Media (CRH)? Use clean, washed pebbles, gravel, or even cut-up pieces of clean kitchen sponge to hold the stem upright.
    """.trimIndent()

    val hydroLayouts = listOf(
        HydroLayoutInfo(
            id = "horizontal",
            title = "LAYOUT A: HORIZONTAL / TABLETOP (The Classic UPLB SNAP Setup)",
            description = "*Best for: Balcony floors, tables, or rooftops with plenty of flat space.*",
            instructions = """
                • Materials: One rectangular opaque plastic basin or styrofoam box (at least 18-Liter capacity) with a fitted lid.
                • Measurements: Cut circular holes in the lid exactly 20 cm apart from each other (for leafy greens like Pechay/Lettuce) or 30 cm apart (for larger plants like Mustard). The holes should be slightly smaller than your 2-inch net cups so they don't fall through.
                • Step-by-Step Assembly:
                1. Prep the Lid: Cut the holes in the lid based on the 20cm/30cm spacing.
                2. Mix Nutrients: Fill the basin with water. Add the UPLB SNAP nutrients (2ml A + 2ml B per Liter). Stir well.
                3. Set the Water Level: Pour the nutrient water into the basin until the water level is exactly 1 cm below the top rim of the basin (or 1 cm below the bottom of the net cup when inserted).
                4. Plant: Fill your net cups with growing media, insert your seedling, and push the cups firmly into the holes in the lid. Done.
            """.trimIndent()
        ),
        HydroLayoutInfo(
            id = "vertical",
            title = "LAYOUT B: VERTICAL / TOWER (The PVC Pipe Setup)",
            description = "*Best for: Very narrow balconies with zero floor space but available vertical wall space.*",
            instructions = """
                • Materials: One 3-inch or 4-inch diameter PVC pipe (about 3 feet tall), an end-cap to seal the bottom, and 2-inch net cups.
                • Measurements: Cut circular holes on the side of the pipe every 8 inches going up. Make the holes just small enough so the net cups fit snugly and don't fall inside.
                • Step-by-Step Assembly:
                1. Seal the Base: Glue or tightly fit the end-cap to the bottom of the PVC pipe so it holds water. Mount the pipe vertically against a wall or railing.
                2. Cut the Holes: Cut the holes every 8 inches up the pipe. 
                3. Fill the Reservoir: Pour your mixed UPLB SNAP nutrient water into the top of the pipe. Fill it up until the water level reaches the **very bottom row of holes**. (Note: Because this is a passive system without a pump, the water only touches the bottom cups. The roots of the top plants will grow down the moist air inside the pipe to reach the water).
                4. Plant: Insert the net cups into the holes. Check the bottom water level weekly and top up with plain water through the top opening.
            """.trimIndent()
        ),
        HydroLayoutInfo(
            id = "hanging",
            title = "LAYOUT C: HANGING (The Suspended Bottle Setup)",
            description = "*Best for: Hanging off balcony railings, ceilings, or eaves.*",
            instructions = """
                • Materials: 1.5-Liter or 2-Liter opaque plastic bottles (like soda or cooking oil bottles), strong string or wire, and 2-inch net cups.
                • Measurements: Cut the plastic bottle completely in half horizontally. You will use the bottom half as the water reservoir and the top half (inverted) as the plant holder.
                • Step-by-Step Assembly:
                1. Cut & Prep: Cut the bottle in half. Poke two small holes on the rim of the bottom half and thread your string/wire through to create a hanger. 
                2. Modify the Top Half: Cut the 2-inch net cup so its rim rests perfectly on the rim of the bottom bottle half. (Or, just invert the top half of the bottle like a funnel and rest it inside the bottom half).
                3. Fill & Hang: Fill the bottom half with your mixed UPLB SNAP nutrient water. Leave exactly 1 cm of air gap below where the net cup will sit. Hang the bottle securely.
                4. Plant: Place your net cup with the seedling and growing media into the opening. Top up with plain water when the level drops.
            """.trimIndent()
        )
    )

    val otherMethods = listOf(
        GrowthMethodInfo(
            id = "potting",
            title = "METHOD 2: CONTAINER POTTING (The \"Balcony Soil\" Method)",
            intro = """
                **What It Is:** Planting in pots using a custom-mixed, lightweight soil blend. Never use 100% regular ground dirt in a pot; it turns into hard mud that suffocates roots.

                **Layout Styles:**
                • Individual Pots: One plant per round pot.
                • Rectangular Planters: Long boxes for a row of onions or lettuce.
                • Vertical Wall Pockets: Fabric pouches hung flat against a wall.

                **Materials & Exact Measurements:**
                • The Pot: Plastic or fabric pots. **Measurement:** 1-gallon (4 Liters) for leafy greens/onions. 3-gallon to 5-gallon (12L to 20L) for fruiting plants like chili. *Must have drainage holes.*
                • The "Balcony 2-1-1" Soil Mix:
                    • 2 Parts Cocopeat: *What it is:* The fine, dusty material left over from processing coconut husks. It acts like a sponge to hold water.
                    • 1 Part Garden Soil: *What it is:* Regular dirt from the ground. It adds weight to anchor the plant so it doesn't blow over.
                    • 1 Part Vermicast: *What it is:* Earthworm manure (worm poop). It is a highly concentrated, natural, slow-release plant food.
                • Exact Measurement Example: To fill one 1-gallon pot, mix: **2 Liters Cocopeat + 1 Liter Garden Soil + 1 Liter Vermicast**.
            """.trimIndent(),
            manual = """
                **Setup & Watering:**
                1. Fill & Plant: Mix the soil, fill the pot leaving 1 inch of space at the top, and plant your seedling.
                2. The Knuckle Test: Stick your finger 1 inch into the soil. If dry, water it. If wet, wait.
                3. Watering Volume: Water slowly at the base until a little drips out the bottom holes. Empty the saucer underneath 30 minutes later.

                **Where to Buy, Alternatives & DIY:**
                • Where to Buy: Shopee/Lazada (search "Cocopeat", "CRH", "Vermicast"). You can also find them in 1kg to 5kg plastic packs at local plant nurseries, agricultural supply stores, hardware stores, and sometimes at the palengke (wet market).
                • Can't find Cocopeat? 
                    • Alternative: Buy a bag of pre-mixed "Potting Soil" from the hardware and use it for the whole mix. 
                    • DIY: Soak a whole, mature coconut husk in a bucket of water for 2 weeks. Pound it with a stick until it breaks apart. The dark, muddy dust that washes out is cocopeat.
                • Can't find Vermicast? 
                    • Alternative: Buy "Complete Fertilizer" (14-14-14) from the hardware. Use just 1 teaspoon mixed into the soil per pot.
                    • DIY: Bury fruit peels, vegetable scraps, and dried leaves in a plastic bucket with some soil. Keep it slightly moist and let it rot for 2 months.
                • Can't find Garden Soil? Just dig up plain topsoil from a nearby park or empty lot (avoid soil that looks sandy or clay-heavy).
            """.trimIndent()
        ),
        GrowthMethodInfo(
            id = "microgreens",
            title = "METHOD 3: MICROGREENS TRAY (The \"7-Day Harvest\" Method)",
            intro = """
                **What It Is:** Growing densely packed "baby" vegetables in a shallow tray and snipping them with scissors just a week after they sprout.

                **Layout Styles:**
                • Single Tray (Flat): One shallow tray sitting flat on a desk or windowsill.
                • Stacked Tray System: Two trays. Top tray has holes (holds seeds), bottom tray has no holes (catches water).

                **Materials & Exact Measurements:**
                • The Trays: Shallow plastic containers at least **2 inches (5 cm) deep**.
                • The Base Layer: **1 inch (2.5 cm)** thick layer of moistened cocopeat, OR a thin hydroponic grow mat.
                • The Seeds: **1 to 2 tablespoons** of seeds (like radish, mung bean, or sunflower) per 10x10 inch tray.
            """.trimIndent(),
            manual = """
                **Setup & Watering:**
                1. Prep: Wet your 1-inch base layer until damp like a wrung-out sponge.
                2. Sow: Sprinkle seeds evenly on top. Do not bury them.
                3. Watering: Spray gently with a misting bottle **1 to 2 times a day**. Keep moist, but never let water pool on top.

                **Where to Buy & Alternatives:**
                • Where to Buy: Search "Microgreens seeds", "Radish seeds", or "Mung bean seeds" on Shopee/Lazada. Grow mats are also sold online as "Hydroponic seed starting mat".
                • No Shallow Tray? Upcycle a plastic bento box, a 1-liter ice cream tub, or a clear plastic cake container. Just poke 5-10 small holes in the bottom for drainage.
                • No Grow Mat or Cocopeat? Use 3 to 4 layers of damp (not dripping wet) paper towels, or a piece of clean, thin cotton cloth (like an old t-shirt) as the base layer.
            """.trimIndent()
        )
    )
}
