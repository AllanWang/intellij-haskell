// This is a generated file. Not intended for manual editing.
package intellij.haskell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import intellij.haskell.psi.HaskellImportEmptySpec;
import intellij.haskell.psi.HaskellVisitor;
import org.jetbrains.annotations.NotNull;

public class HaskellImportEmptySpecImpl extends HaskellCompositeElementImpl implements HaskellImportEmptySpec {

  public HaskellImportEmptySpecImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull HaskellVisitor visitor) {
    visitor.visitImportEmptySpec(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof HaskellVisitor) accept((HaskellVisitor) visitor);
    else super.accept(visitor);
  }

}
