// This is a generated file. Not intended for manual editing.
package intellij.haskell.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static intellij.haskell.psi.HaskellTypes.*;
import intellij.haskell.psi.*;
import com.intellij.navigation.ItemPresentation;
import scala.Option;
import scala.collection.Seq;

public class HaskellDataDeclarationImpl extends HaskellCompositeElementImpl implements HaskellDataDeclaration {

  public HaskellDataDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull HaskellVisitor visitor) {
    visitor.visitDataDeclaration(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof HaskellVisitor) accept((HaskellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaskellCcontext getCcontext() {
    return PsiTreeUtil.getChildOfType(this, HaskellCcontext.class);
  }

  @Override
  @NotNull
  public List<HaskellConstr> getConstrList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaskellConstr.class);
  }

  @Override
  @Nullable
  public HaskellDataDeclarationDeriving getDataDeclarationDeriving() {
    return PsiTreeUtil.getChildOfType(this, HaskellDataDeclarationDeriving.class);
  }

  @Override
  @NotNull
  public List<HaskellKindSignature> getKindSignatureList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaskellKindSignature.class);
  }

  @Override
  @Nullable
  public HaskellPragma getPragma() {
    return PsiTreeUtil.getChildOfType(this, HaskellPragma.class);
  }

  @Override
  @NotNull
  public List<HaskellQName> getQNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaskellQName.class);
  }

  @Override
  @NotNull
  public HaskellSimpletype getSimpletype() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, HaskellSimpletype.class));
  }

  @Override
  @Nullable
  public HaskellTtype getTtype() {
    return PsiTreeUtil.getChildOfType(this, HaskellTtype.class);
  }

  @Override
  @NotNull
  public List<HaskellTypeSignature> getTypeSignatureList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaskellTypeSignature.class);
  }

  @Override
  public String getName() {
    return HaskellPsiImplUtil.getName(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return HaskellPsiImplUtil.getPresentation(this);
  }

  @Override
  public Seq<HaskellNamedElement> getIdentifierElements() {
    return HaskellPsiImplUtil.getIdentifierElements(this);
  }

  @Override
  public Option<String> getModuleName() {
    return HaskellPsiImplUtil.getModuleName(this);
  }

  @Override
  public HaskellNamedElement getDataTypeConstructor() {
    return HaskellPsiImplUtil.getDataTypeConstructor(this);
  }

}
