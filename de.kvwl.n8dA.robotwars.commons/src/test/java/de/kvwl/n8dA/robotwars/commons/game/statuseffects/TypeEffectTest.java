package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;


public class TypeEffectTest {
	
	private TypeEffect typeEffect;
	
	private TypeEffect otherStatusEffect;
	
	@Before
	public void setUp()
	{
		this.typeEffect = new TypeEffect();
		this.typeEffect.setId(80);
		this.typeEffect.setRoundsLeft(1);
		this.typeEffect.setStartDuration(1);

		
		this.otherStatusEffect = new TypeEffect();
		this.otherStatusEffect.setId(81);
		this.otherStatusEffect.setRoundsLeft(1);
		this.otherStatusEffect.setStartDuration(1);
	}
	
	@Test
	public void testResolveVUL_Sci_And_VUL_Sci() throws Exception {
		
		typeEffect.setActionType(RobotActionType.LIGHTNING);
		typeEffect.setModificationType(TypeEffectModificationType.VULNERABILITY);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.LIGHTNING);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.VULNERABILITY);
		
		typeEffect.resolveInteractionWith(otherStatusEffect);
		
		int roundsLeft = typeEffect.getRoundsLeft();
		
		assertEquals(2, roundsLeft);
	}
	
	@Test
	public void testResolveRES_Rock_And_RES_Rock() throws Exception {
		
		typeEffect.setActionType(RobotActionType.FIRE);
		typeEffect.setModificationType(TypeEffectModificationType.RESISTANCE);
		typeEffect.setId(100L);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.FIRE);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.RESISTANCE);
		otherStatusEffect.setId(100L);
		
		typeEffect.resolveInteractionWith(otherStatusEffect);
		
		
		int roundsLeft = typeEffect.getRoundsLeft();
		
		assertEquals(2, roundsLeft);
	}
	@Test
	public void testResolveVUL_Sci_And_RES_Sci() throws Exception {
		
		typeEffect.setActionType(RobotActionType.LIGHTNING);
		typeEffect.setModificationType(TypeEffectModificationType.VULNERABILITY);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.LIGHTNING);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.RESISTANCE);
		
		typeEffect.resolveInteractionWith(otherStatusEffect);
		
		int roundsLeft = typeEffect.getRoundsLeft();
		
		assertEquals(0, roundsLeft);
	}
	
	
	
	@Test
	public void testResolveVUL_Sci_And_RES_Sci_newEffect() throws Exception {
		
		typeEffect.setActionType(RobotActionType.LIGHTNING);
		typeEffect.setModificationType(TypeEffectModificationType.VULNERABILITY);
		otherStatusEffect.setRoundsLeft(2);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.LIGHTNING);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.RESISTANCE);
		
		boolean resolveInteractionWith = typeEffect.resolveInteractionWith(otherStatusEffect);
		assertFalse(resolveInteractionWith);
		
		TypeEffect expectedEffect = new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.RESISTANCE, 1);
		assertEquals(expectedEffect , otherStatusEffect);
		
		int roundsLeft = typeEffect.getRoundsLeft();
		
		assertEquals(-1, roundsLeft);
	}
	@Test
	public void testResolveVUL_Rock_And_RES_Sci_newEffect() throws Exception {
		
		typeEffect.setActionType(RobotActionType.FIRE);
		typeEffect.setModificationType(TypeEffectModificationType.VULNERABILITY);
		
		TypeEffect expectedEffect = new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.VULNERABILITY, 1);
		TypeEffect expectedOtherEffect = new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.RESISTANCE, 2);
		
		otherStatusEffect.setRoundsLeft(2);
		((TypeEffect) otherStatusEffect).setActionType(RobotActionType.LIGHTNING);
		((TypeEffect) otherStatusEffect).setModificationType(TypeEffectModificationType.RESISTANCE);
		
		
		boolean resolveInteractionWith = typeEffect.resolveInteractionWith(otherStatusEffect);
		
		assertFalse(resolveInteractionWith);
		
		assertEquals(expectedEffect , typeEffect);
		assertEquals(expectedOtherEffect , otherStatusEffect);
	}

}
