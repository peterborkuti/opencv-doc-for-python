package hu.borkutip.opencvdoc;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenCVDocumentationProviderTest {
    private static final String URL_PREFIX="https://docs.opencv.org/4.x/";
    @Test
    void noIndexFile() {
        assertThrows(Throwable.class, () -> new OpenCVDocumentationProvider(""));
    }

    @Test
    void IndexFileExists() {
        OpenCVDocumentationProvider p = new OpenCVDocumentationProvider("/test_index.csv");
        assertNotNull(p);
    }

    @Test
    void keyConvertedToLowerButNotValue() {
        OpenCVDocumentationProvider p = new OpenCVDocumentationProvider(List.of("KEY,VALUE"));
        MockPsiElement element = new MockPsiElement("");
        MockPsiElement originalElement = new MockPsiElement("Key");

        OpenCVDocumentationTarget target = (OpenCVDocumentationTarget) p.documentationTarget(element, originalElement);

        assertEquals("key", target.getKeyword());
        assertEquals(URL_PREFIX + "VALUE", target.getUrl());
    }

    @Test
    void keyNotFound() {
        OpenCVDocumentationProvider p = new OpenCVDocumentationProvider(List.of("KEY,VALUE"));
        MockPsiElement element = new MockPsiElement("");
        MockPsiElement originalElement = new MockPsiElement("OtherKey");

        OpenCVDocumentationTarget target = (OpenCVDocumentationTarget) p.documentationTarget(element, originalElement);

        assertNull(target);
    }

}

class MockPsiElement implements PsiElement {
    String text;
    MockPsiElement(String text) {
        this.text = text;
    }
    @Override
    public @NotNull Project getProject() throws PsiInvalidElementAccessException {
        return null;
    }

    @Override
    public @NotNull Language getLanguage() {
        return null;
    }

    @Override
    public PsiManager getManager() {
        return null;
    }

    @Override
    public @NotNull PsiElement @NotNull [] getChildren() {
        return new PsiElement[0];
    }

    @Override
    public PsiElement getParent() {
        return null;
    }

    @Override
    public PsiElement getFirstChild() {
        return null;
    }

    @Override
    public PsiElement getLastChild() {
        return null;
    }

    @Override
    public PsiElement getNextSibling() {
        return null;
    }

    @Override
    public PsiElement getPrevSibling() {
        return null;
    }

    @Override
    public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
        return null;
    }

    @Override
    public TextRange getTextRange() {
        return null;
    }

    @Override
    public int getStartOffsetInParent() {
        return 0;
    }

    @Override
    public int getTextLength() {
        return 0;
    }

    @Override
    public @Nullable PsiElement findElementAt(int i) {
        return null;
    }

    @Override
    public @Nullable PsiReference findReferenceAt(int i) {
        return null;
    }

    @Override
    public int getTextOffset() {
        return 0;
    }

    @Override
    public @NlsSafe String getText() {
        return this.text;
    }

    @Override
    public char @NotNull [] textToCharArray() {
        return new char[0];
    }

    @Override
    public PsiElement getNavigationElement() {
        return null;
    }

    @Override
    public PsiElement getOriginalElement() {
        return null;
    }

    @Override
    public boolean textMatches(@NotNull @NonNls CharSequence charSequence) {
        return false;
    }

    @Override
    public boolean textMatches(@NotNull PsiElement psiElement) {
        return false;
    }

    @Override
    public boolean textContains(char c) {
        return false;
    }

    @Override
    public void accept(@NotNull PsiElementVisitor psiElementVisitor) {

    }

    @Override
    public void acceptChildren(@NotNull PsiElementVisitor psiElementVisitor) {

    }

    @Override
    public PsiElement copy() {
        return null;
    }

    @Override
    public PsiElement add(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement addBefore(@NotNull PsiElement psiElement, @Nullable PsiElement psiElement1) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement addAfter(@NotNull PsiElement psiElement, @Nullable PsiElement psiElement1) throws IncorrectOperationException {
        return null;
    }

    @Override
    public void checkAdd(@NotNull PsiElement psiElement) throws IncorrectOperationException {

    }

    @Override
    public PsiElement addRange(PsiElement psiElement, PsiElement psiElement1) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement addRangeBefore(@NotNull PsiElement psiElement, @NotNull PsiElement psiElement1, PsiElement psiElement2) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement addRangeAfter(PsiElement psiElement, PsiElement psiElement1, PsiElement psiElement2) throws IncorrectOperationException {
        return null;
    }

    @Override
    public void delete() throws IncorrectOperationException {

    }

    @Override
    public void checkDelete() throws IncorrectOperationException {

    }

    @Override
    public void deleteChildRange(PsiElement psiElement, PsiElement psiElement1) throws IncorrectOperationException {

    }

    @Override
    public PsiElement replace(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public @Nullable PsiReference getReference() {
        return null;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return new PsiReference[0];
    }

    @Override
    public <T> @Nullable T getCopyableUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putCopyableUserData(@NotNull Key<T> key, @Nullable T t) {

    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor psiScopeProcessor, @NotNull ResolveState resolveState, @Nullable PsiElement psiElement, @NotNull PsiElement psiElement1) {
        return false;
    }

    @Override
    public @Nullable PsiElement getContext() {
        return null;
    }

    @Override
    public boolean isPhysical() {
        return false;
    }

    @Override
    public @NotNull GlobalSearchScope getResolveScope() {
        return null;
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return null;
    }

    @Override
    public ASTNode getNode() {
        return null;
    }

    @Override
    public boolean isEquivalentTo(PsiElement psiElement) {
        return false;
    }

    @Override
    public Icon getIcon(int i) {
        return null;
    }

    @Override
    public <T> @Nullable T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

    }
}