package ga.hhfed.masochistsplitspace

interface Enemy: ExtraObject {
    override fun touchEffect(ship: Ship) { ship.explode() }
}